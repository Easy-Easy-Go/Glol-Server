package com.server.glol.domain.league.service.impl

import com.server.glol.domain.league.dto.LeagueDto
import com.server.glol.domain.league.entities.League
import com.server.glol.domain.league.repository.LeagueCustomRepository
import com.server.glol.domain.league.repository.LeagueRepository
import com.server.glol.domain.league.service.LeagueService
import com.server.glol.domain.league.service.facade.RemoteLeagueFacade
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.service.RemoteSummonerFacade
import org.springframework.stereotype.Service
import javax.transaction.Transactional

private const val FREE_RANK = "RANKED_FLEX_SR"
private const val SOLO_RANK = "RANKED_SOLO_5x5"

@Service
class LeagueServiceImpl(
    private val remoteLeagueFacade: RemoteLeagueFacade,
    private val summonerRepository: SummonerRepository,
    private val remoteSummonerFacade: RemoteSummonerFacade,
    private val leagueCustomRepository: LeagueCustomRepository,
    private val leagueRepository: LeagueRepository,
) : LeagueService {

    override fun getLeague(name: String): MutableSet<LeagueDto> =
        if (!summonerRepository.existsSummonerByName(name)) {
            val summoner = remoteSummonerFacade.getSummonerByName(name)

            remoteLeagueFacade.getLeague(summoner.id)
        } else {
            leagueCustomRepository.getLeagueEntryByName(name)
        }


    @Transactional
    override fun saveLeague(name: String) {
        val soloLeague = leagueRepository.findLeagueBySummonerNameAndQueueType(name, SOLO_RANK)
            ?: League()
        val freeLeague = leagueRepository.findLeagueBySummonerNameAndQueueType(name, FREE_RANK)
            ?: League()

        val findSummoner = summonerRepository.findSummonerByName(name)

        getLeague(name).forEach { league ->
            if (league.queueType == SOLO_RANK)
                soloLeague.leagueUpdate(league, findSummoner)
            if (league.queueType == FREE_RANK)
                freeLeague.leagueUpdate(league, findSummoner)
        }

        leagueRepository.saveAll(mutableSetOf(soloLeague, freeLeague))
    }
}