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
        if (isExistsSummonerProfile(name))
            return summonerProfileCustomRepository.getSummonerProfileByName(name)
        else {
            log.debug("${NOT_FOUND_SUMMONER_PROFILE.msg} in getLeague")
            throw CustomException(NOT_FOUND_SUMMONER_PROFILE)
        }
    }

    @Transactional
    override fun saveSummonerProfile(name: String) {
        val soloSummonerProfile =
            summonerProfileCustomRepository.getSummonerProfileByQueueTypeAndName(name, SOLO_RANK) ?: SummonerProfile()
        val freeSummonerProfile =
            summonerProfileCustomRepository.getSummonerProfileByQueueTypeAndName(name, FREE_RANK) ?: SummonerProfile()
        val findSummoner = summonerRepository.findSummonerByName(name)!!
        val getLeague = getLeagueByName(findSummoner.name)

        updateSummonerId(getLeague, findSummoner)

        summonerProfileUpdate(soloSummonerProfile, freeSummonerProfile, findSummoner, getLeague)

        summonerProfileRepository.saveAll(mutableSetOf(soloSummonerProfile, freeSummonerProfile))
    }

    private fun updateSummonerId(summonerProfileDto: MutableSet<SummonerProfileDto>, findSummoner: Summoner) {
        summonerProfileDto.elementAt(0).let { findSummoner.updateSummonerId(it.summonerId) }
    }

    private fun isExistsSummonerProfile(name: String): Boolean = summonerProfileRepository.existsBySummonerName(name)

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
        soloSummonerProfile.summonerProfileUpdate(
            summonerProfile.elementAt(0).setGamesAndWinRate().rankSetting().rankScoreRecord(),
            findSummoner
        )
        freeSummonerProfile.summonerProfileUpdate(
            summonerProfile.elementAt(1).setGamesAndWinRate().rankSetting().rankScoreRecord(),
            findSummoner
        )
    }

    private fun SummonerProfileDto.setGamesAndWinRate(): SummonerProfileDto {
        this.games = this.wins + this.losses
        this.winRate = (this.wins / this.games.toDouble() * 100).roundToInt()

        return this
    }

    private fun SummonerProfileDto.rankSetting(): SummonerProfileDto {
        this.queueType = when (this.queueType) {
            "RANKED_FLEX_SR" -> "자유랭크"
            "RANKED_SOLO_5x5" -> "솔로랭크"
            else -> {
                "기타"
            }
        }

        return this
    }

    private fun SummonerProfileDto.rankScoreRecord(): SummonerProfileDto {
        this.rankScore = this.convertTier() + this.convertRank()

        return this
    }

    private fun SummonerProfileDto.convertRank(): Int =
        when (tier) {
            "I" -> 1
            "II" -> 2
            "III" -> 3
            "IV" -> 4
            else -> {
                0
            }
        }

    private fun SummonerProfileDto.convertTier(): Int = when (rank) {
        "Iron" -> 0
        "Bronze" -> 5
        "Silver" -> 10
        "Gold" -> 15
        "Platinum" -> 20
        "Diamond" -> 25
        "Master" -> 30
        "Grandmaster" -> 35
        "Challenger" -> 40
        else -> {
            100
        }
    }
}