package com.server.glol.domain.summonerProfile.service.impl

import com.server.glol.domain.summonerProfile.dto.SummonerProfileDto
import com.server.glol.domain.summonerProfile.dto.projection.SummonerProfileResponse
import com.server.glol.domain.summonerProfile.entities.SummonerProfile
import com.server.glol.domain.summonerProfile.repository.SummonerProfileCustomRepository
import com.server.glol.domain.summonerProfile.repository.SummonerProfileRepository
import com.server.glol.domain.summonerProfile.service.SummonerProfileService
import com.server.glol.domain.summonerProfile.service.facade.RemoteSummonerProfileFacade
import com.server.glol.domain.summoner.entities.Summoner
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER_PROFILE
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.math.roundToInt

private const val FREE_RANK = "RANKED_FLEX_SR"
private const val SOLO_RANK = "RANKED_SOLO_5x5"

@Service
class SummonerProfileServiceImpl(
    private val remoteSummonerProfileFacade: RemoteSummonerProfileFacade,
    private val summonerRepository: SummonerRepository,
    private val summonerProfileCustomRepository: SummonerProfileCustomRepository,
    private val summonerProfileRepository: SummonerProfileRepository,
) : SummonerProfileService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun getSummonerProfile(name: String): MutableSet<SummonerProfileResponse> {
        if (isExistsLeague(name))
            return summonerProfileCustomRepository.getSummonerProfileByName(name)
        else {
            log.debug("${NOT_FOUND_SUMMONER_PROFILE.msg} in getLeague")
            throw CustomException(NOT_FOUND_SUMMONER_PROFILE)
        }
    }

    @Transactional
    override fun saveSummonerProfile(name: String) {
        val soloSummonerProfile = summonerProfileCustomRepository.getSummonerProfile(name, SOLO_RANK) ?: SummonerProfile()
        val freeSummonerProfile = summonerProfileCustomRepository.getSummonerProfile(name, FREE_RANK) ?: SummonerProfile()
        val findSummoner = summonerRepository.findSummonerByName(name)!!
        val getLeague = getLeagueByName(findSummoner.name)

        updateSummonerId(getLeague, findSummoner)

        summonerProfileUpdate(soloSummonerProfile, freeSummonerProfile, findSummoner, getLeague)

        summonerProfileRepository.saveAll(mutableSetOf(soloSummonerProfile, freeSummonerProfile))
    }

    private fun updateSummonerId(summonerProfileDto: MutableSet<SummonerProfileDto>, findSummoner: Summoner) {
        summonerProfileDto.elementAt(0).let { findSummoner.updateSummonerId(it.summonerId) }
    }

    private fun isExistsLeague(name: String): Boolean = summonerProfileRepository.existsBySummonerName(name)

    private fun isExistsSummoner(name: String): Boolean = summonerRepository.existsSummonerByName(name)

    private fun getLeagueByName(name: String): MutableSet<SummonerProfileDto> =
        if (isExistsSummoner(name)) {
            summonerRepository.findSummonerByName(name)
                .let { summoner -> remoteSummonerProfileFacade.getSummonerProfile(summoner!!.id) }
        } else {
            log.debug("${NOT_FOUND_SUMMONER.msg} in getLeagueByName")
            throw CustomException(NOT_FOUND_SUMMONER)
        }

    private fun summonerProfileUpdate(
        soloSummonerProfile: SummonerProfile,
        freeSummonerProfile: SummonerProfile,
        findSummoner: Summoner,
        summonerProfile: MutableSet<SummonerProfileDto>
    ) {
        soloSummonerProfile.summonerProfileUpdate(summonerProfile.elementAt(0).calculate(), findSummoner)
        freeSummonerProfile.summonerProfileUpdate(summonerProfile.elementAt(1).calculate(), findSummoner)
    }

    private fun SummonerProfileDto.calculate(): SummonerProfileDto {
        this.games = this.wins + this.losses
        this.winRate = (this.wins / this.games.toDouble() * 100).roundToInt()

        return this
    }
}