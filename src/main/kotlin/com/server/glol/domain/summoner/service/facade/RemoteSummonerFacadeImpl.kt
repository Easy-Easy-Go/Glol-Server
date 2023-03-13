package com.server.glol.domain.summoner.service.facade

import com.server.glol.domain.summoner.dto.projection.SummonerDto
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RemoteSummonerFacadeImpl(
    private val webClient: WebClient,
) : RemoteSummonerFacade {

    override fun getSummonerByName(name: String): SummonerDto {
        return webClient.mutate().build()
            .get().uri(RiotProperties.SUMMONER_API_URL + name)
            .retrieve()
            .bodyToMono(SummonerDto().javaClass)
            .block()!!
    }

    override fun getSummonerByPuuid(puuid: String): SummonerDto {
        return webClient.mutate().build()
            .get().uri(RiotProperties.SUMMONER_BY_PUUID_URL + puuid)
            .retrieve()
            .bodyToMono(SummonerDto().javaClass)
            .block()!!
    }
}