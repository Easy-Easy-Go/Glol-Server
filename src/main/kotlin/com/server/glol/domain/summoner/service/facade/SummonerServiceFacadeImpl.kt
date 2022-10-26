package com.server.glol.domain.summoner.service.facade

import com.server.glol.domain.summoner.repository.SummonerCustomRepository
import com.server.glol.domain.summoner.repository.projection.SummonerVo
import com.server.glol.domain.summoner.service.SummonerServiceFacade
import com.server.glol.global.config.banned.BannedAccountConfig
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.http.MediaType
import java.nio.charset.StandardCharsets

@Service
class SummonerServiceFacadeImpl(
    private val riotProperties: RiotProperties,
    private val summonerCustomRepository: SummonerCustomRepository,
): SummonerServiceFacade {

    override fun getPuuid(name: String): String {
        return WebClient.create().get().uri(riotProperties.summonerAPIUrl + name).headers {
            it.contentType = MediaType.APPLICATION_JSON
            it.acceptCharset = listOf(StandardCharsets.UTF_8)
            it.set("X-Riot-Token", riotProperties.secretKey)
            it.set("Origin", riotProperties.origin)
        }.retrieve().bodyToMono(String().javaClass)
            .onErrorReturn(summonerCustomRepository.findPuuidByName(BannedAccountConfig.name)!!).block()
            ?: throw IllegalArgumentException("Not Found Summoner")
    }

    override fun getSummoner(name: String): SummonerVo {
        val summonerVo = WebClient.create().get().uri(riotProperties.summonerAPIUrl + name).headers {
            it.contentType = MediaType.APPLICATION_JSON
            it.acceptCharset = listOf(StandardCharsets.UTF_8)
            it.set("X-Riot-Token", riotProperties.secretKey)
            it.set("Origin", riotProperties.origin)
        }.retrieve().bodyToMono(SummonerVo().javaClass)
            .onErrorReturn(summonerCustomRepository.findSummonerByName(BannedAccountConfig.name)!!).block()
            ?: throw IllegalArgumentException("Not Found Summoner")

        return summonerVo
    }
}