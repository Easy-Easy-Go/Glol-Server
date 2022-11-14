package com.server.glol.domain.league.service.impl

import com.server.glol.domain.league.dto.LeagueDto
import com.server.glol.domain.league.entities.League
import com.server.glol.domain.league.repository.LeagueCustomRepository
import com.server.glol.domain.league.repository.LeagueRepository
import com.server.glol.domain.league.service.LeagueService
import com.server.glol.domain.league.service.facade.RemoteLeagueServiceFacade
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.service.RemoteSummonerServiceFacade
import org.springframework.stereotype.Service
import javax.transaction.Transactional

private const val FREE_RANK = "RANKED_FLEX_SR"
private const val SOLO_RANK = "RANKED_SOLO_5x5"

@Service
class LeagueServiceImpl(
    private val remoteLeagueServiceFacade: RemoteLeagueServiceFacade,
    private val summonerRepository: SummonerRepository,
    private val remoteSummonerServiceFacade: RemoteSummonerServiceFacade,
    private val leagueCustomRepository: LeagueCustomRepository,
    private val leagueRepository: LeagueRepository,
) : LeagueService {

    override fun getLeague(name: String): MutableSet<LeagueDto> =
        if (!summonerRepository.existsSummonerByName(name)) {
            val summoner = remoteSummonerServiceFacade.getSummoner(name)

            remoteLeagueServiceFacade.getLeague(summoner.id)
        } else {
            leagueCustomRepository.getLeagueEntryByName(name)
        }


    @Transactional
    override fun saveLeague(name: String, leagueDto: MutableSet<LeagueDto>) {
        val soloLeague: League = leagueCustomRepository.getLeague(name, SOLO_RANK)
            ?: League()
        val freeLeague: League = leagueCustomRepository.getLeague(name, FREE_RANK)
            ?: League()

        val findSummoner = summonerRepository.findSummonerByName(name)!!

        leagueDto.map { dto ->
            if (dto.queueType == SOLO_RANK) {
                soloLeague.leagueUpdate(dto, findSummoner)
            }
            if (dto.queueType == FREE_RANK) {
                freeLeague.leagueUpdate(dto, findSummoner)
            }
        }

        leagueRepository.saveAll(mutableListOf(soloLeague, freeLeague))
    }
}