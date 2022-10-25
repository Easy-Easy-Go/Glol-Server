package com.server.glol.domain.summoner.service.impl

import com.server.glol.domain.summoner.entities.Summoner
import com.server.glol.domain.summoner.repository.SummonerCustomRepository
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.repository.projection.SummonerVo
import com.server.glol.domain.summoner.service.SummonerService
import com.server.glol.domain.summoner.service.SummonerServiceFacade
import com.server.glol.global.config.banned.BannedAccountConfig
import org.springframework.stereotype.Service

@Service
class SummonerServiceImpl(
    private val summonerRepository: SummonerRepository,
    private val summonerCustomRepository: SummonerCustomRepository,
    private val summonerServiceFacade: SummonerServiceFacade,
) : SummonerService {

    override fun registerSummoner(name: String) {
        val summoner = summonerCustomRepository.findSummonerByName(name)
            ?: summonerServiceFacade.getSummoner(name)

        save(summoner)
    }

    private fun save(summoner: SummonerVo?) {
        if (summoner != null && !summoner.visited && summoner.name != BannedAccountConfig.name) {
            summonerRepository.save(
                Summoner(
                    id = summoner.id,
                    accountId = summoner.accountId,
                    name = summoner.name,
                    puuid = summoner.puuid,
                    profileIconId = summoner.profileIconId,
                    visited = true
                )
            )
        }
    }

}