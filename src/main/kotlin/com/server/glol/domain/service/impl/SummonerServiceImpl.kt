package com.server.glol.domain.service.impl

import com.server.glol.domain.dto.SummonerResponseDto
import com.server.glol.domain.service.SummonerService
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class SummonerServiceImpl
constructor(
    private val riotProperties: RiotProperties
): SummonerService {

    @Cacheable(key = "#name", cacheNames = ["summonerStorage"])
    override fun getSummonerAccount(name: String): SummonerResponseDto? {
        return WebClient.create()
            .get()
            .uri(riotProperties.summonerAPIUrl + name)
            .acceptCharset(charset("UTF-8"))
            .header("X-Riot-Token", riotProperties.secretKey)
            .header("Origin", riotProperties.origin)
            .retrieve()
            .bodyToMono(SummonerResponseDto().javaClass)
            .block()
    }
}