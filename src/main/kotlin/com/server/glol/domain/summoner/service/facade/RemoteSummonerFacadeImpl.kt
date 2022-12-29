package com.server.glol.domain.summoner.service.facade

import com.server.glol.domain.summoner.dto.projection.SummonerDto
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RemoteSummonerFacadeImpl(
    private val riotProperties: RiotProperties,
    private val webClient: WebClient,
) : RemoteSummonerFacade {

    override fun getSummonerByName(name: String): SummonerDto {
        return webClient.mutate().build()
            .get().uri(riotProperties.summonerAPIUrl + name)
            .retrieve()
            .bodyToMono(SummonerDto().javaClass)
            .block()!!
    }

    override fun getSummonerByPuuid(puuid: String): SummonerDto {
        return webClient.mutate().build()
            .get().uri(riotProperties.summonerByPuuidAPIURL + puuid)
            .retrieve()
            .bodyToMono(SummonerDto().javaClass)
            .block()!!
    }
}