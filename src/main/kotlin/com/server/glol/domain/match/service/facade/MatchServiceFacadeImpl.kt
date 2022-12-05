package com.server.glol.domain.match.service.facade

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.service.MatchServiceFacade
import com.server.glol.global.config.properties.RiotProperties
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class MatchServiceFacadeImpl(
    private val riotProperties: RiotProperties,
    private val webClient: WebClient,
) : MatchServiceFacade {

    override fun getMatchIds(puuid: String, matchPageable: MatchPageable): MutableList<String> {
        val matchId: MutableList<String> = mutableListOf()

        return webClient.mutate().build().get()
            .uri(riotProperties.matchUUIDUrl + puuid + "/ids?queue=" + matchPageable.queue + "&count=" + matchPageable.count)
            .retrieve().bodyToMono(matchId::class.java).block()
            ?: throw CustomException(ErrorCode.NOT_FOUND_MATCH)
    }

    override fun getMatch(matchId: String): MatchDto =
        webClient.mutate().build().get().uri(riotProperties.matchesMatchIdUrl + matchId)
            .retrieve().bodyToMono(MatchDto().javaClass).block()
            ?: throw CustomException(ErrorCode.NOT_FOUND_MATCH)
}