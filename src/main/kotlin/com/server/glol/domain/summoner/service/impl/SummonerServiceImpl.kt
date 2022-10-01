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
class SummonerServiceImpl
constructor(
    private val riotProperties: RiotProperties,
    private val summonerRepository: SummonerRepository,
    private val summonerCustomRepository: SummonerCustomRepository
) : SummonerService {

    override fun registrationSummoner(name: String) {
        val summoner = summonerCustomRepository.findSummonerByName(name) ?: getSummoner(name)

        save(summoner)
    }

    private fun getSummoner(name: String): SummonerVo {
        return WebClient.create().get().uri(riotProperties.summonerAPIUrl + name).headers {
                it.contentType = MediaType.APPLICATION_JSON
                it.acceptCharset = listOf(StandardCharsets.UTF_8)
                it.set("X-Riot-Token", riotProperties.secretKey)
                it.set("Origin", riotProperties.origin)
            }.retrieve().bodyToMono(SummonerVo().javaClass).block()
            ?: throw IllegalArgumentException("Not Exists Summoner")
    }

    private fun save(summoner: SummonerVo?) {
        if (summoner != null && !summoner.visited) {
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
        } else {
            throw IllegalArgumentException("Already Exists Summoner")
        }
    }

}