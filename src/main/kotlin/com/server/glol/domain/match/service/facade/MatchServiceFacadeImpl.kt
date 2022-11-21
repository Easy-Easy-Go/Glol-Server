package com.server.glol.domain.match.service.facade

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.service.MatchServiceFacade
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.StandardCharsets

@Service
class MatchServiceFacadeImpl(
    private val riotProperties: RiotProperties,
): MatchServiceFacade {

    override fun getMatchIds(puuid: String, matchPageable: MatchPageable): MutableList<String> {
        val matchId: MutableList<String> = mutableListOf()

        return WebClient.create().get()
            .uri(riotProperties.matchUUIDUrl + puuid + "/ids?queue=" + matchPageable.queue + "&count=" + matchPageable.count)
            .headers { httpHeaders ->
                httpHeaders.contentType = MediaType.APPLICATION_JSON
                httpHeaders.acceptCharset = listOf(StandardCharsets.UTF_8)
                httpHeaders.set("X-Riot-Token", riotProperties.secretKey)
                httpHeaders.set("Origin", riotProperties.origin)
            }.retrieve().bodyToMono(matchId::class.java).block()
            ?: throw IllegalArgumentException("Not Exists Matches")
    }

    override fun getMatch(matchId: String): MatchDto
        = WebClient.create().get().uri(riotProperties.matchesMatchIdUrl + matchId).headers { httpHeaders ->
            httpHeaders.contentType = MediaType.APPLICATION_JSON
            httpHeaders.acceptCharset = listOf(StandardCharsets.UTF_8)
            httpHeaders.set("X-Riot-Token", riotProperties.secretKey)
            httpHeaders.set("Origin", riotProperties.origin)
        }.retrieve().bodyToMono(MatchDto().javaClass).block()
            ?: throw IllegalArgumentException("Not Exists Match")
}