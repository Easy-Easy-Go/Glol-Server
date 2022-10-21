package com.server.glol.domain.summoner.service.impl

import com.server.glol.domain.summoner.entites.Summoner
import com.server.glol.domain.summoner.repository.SummonerCustomRepository
import com.server.glol.domain.summoner.repository.SummonerRepository
import com.server.glol.domain.summoner.repository.projection.SummonerVo
import com.server.glol.domain.summoner.service.SummonerService
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.StandardCharsets

@Service
class SummonerServiceImpl(
    private val riotProperties: RiotProperties,
    private val summonerRepository: SummonerRepository,
    private val summonerCustomRepository: SummonerCustomRepository
) : SummonerService {

    override fun registerSummoner(name: String): SummonerVo {
        val summoner = summonerCustomRepository.findSummonerByName(name) ?: getSummoner(name)

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