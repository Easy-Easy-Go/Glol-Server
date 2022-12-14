package com.server.glol.domain.league.service.impl

import com.server.glol.domain.league.dto.LeagueDto
import com.server.glol.domain.league.dto.projection.LeagueResponse
import com.server.glol.domain.league.entities.League
import com.server.glol.domain.league.repository.LeagueCustomRepository
import com.server.glol.domain.league.repository.LeagueRepository
import com.server.glol.domain.league.service.LeagueService
import com.server.glol.domain.league.service.facade.RemoteLeagueFacade
import com.server.glol.domain.summoner.entities.Summoner
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_LEAGUE
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.transaction.Transactional

private const val FREE_RANK = "RANKED_FLEX_SR"
private const val SOLO_RANK = "RANKED_SOLO_5x5"

@Service
class LeagueServiceImpl(
    private val remoteLeagueFacade: RemoteLeagueFacade,
    private val summonerRepository: SummonerRepository,
    private val leagueCustomRepository: LeagueCustomRepository,
    private val leagueRepository: LeagueRepository,
) : LeagueService {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun getLeague(name: String): MutableSet<LeagueResponse> {
        if (isExistsLeague(name))
            return leagueCustomRepository.getLeagueEntryByName(name)
        else {
            log.debug("${NOT_FOUND_LEAGUE.msg} in getLeague")
            throw CustomException(NOT_FOUND_LEAGUE)
        }
    }

    @Transactional
    override fun saveLeague(name: String) {
        val soloLeague = leagueCustomRepository.getLeague(name, SOLO_RANK) ?: League()
        val freeLeague = leagueCustomRepository.getLeague(name, FREE_RANK) ?: League()
        val findSummoner = summonerRepository.findSummonerByName(name)!!
        val getLeague = getLeagueByName(findSummoner.name)

        updateSummonerId(getLeague, findSummoner)

        leagueUpdate(soloLeague, freeLeague, findSummoner, getLeague)

        leagueRepository.saveAll(mutableSetOf(soloLeague, freeLeague))
    }

    private fun updateSummonerId(leagueDto: MutableSet<LeagueDto>, findSummoner: Summoner) {
        leagueDto.elementAt(0).let { findSummoner.updateSummonerId(it.summonerId) }
    }

    private fun isExistsLeague(name: String): Boolean = leagueRepository.existsBySummonerName(name)

    private fun isExistsSummoner(name: String): Boolean = summonerRepository.existsSummonerByName(name)

    private fun getLeagueByName(name: String): MutableSet<LeagueDto> =
        if (isExistsSummoner(name)) {
            summonerRepository.findSummonerByName(name)
                .let { summoner -> remoteLeagueFacade.getLeague(summoner!!.id) }
        } else {
            log.debug("${NOT_FOUND_SUMMONER.msg} in getLeagueByName")
            throw CustomException(NOT_FOUND_SUMMONER)
        }

    private fun leagueUpdate(
        soloLeague: League,
        freeLeague: League,
        findSummoner: Summoner,
        league: MutableSet<LeagueDto>
    ) {
        soloLeague.leagueUpdate(league.elementAt(0), findSummoner)
        freeLeague.leagueUpdate(league.elementAt(1), findSummoner)
    }
}