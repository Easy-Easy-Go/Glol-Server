package com.server.glol.domain.match.service.facade.impl

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.service.facade.RemoteMatchServiceFacade
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class
RemoteMatchServiceFacadeImpl(
    private val riotProperties: RiotProperties,
    private val webClient: WebClient,
) : RemoteMatchServiceFacade {

    override fun getMatchIds(puuid: String, matchPageable: MatchPageable): MutableList<String> {
        val matchIds: MutableList<String> = mutableListOf()

        return webClient.mutate().build()
            .get()
            .uri(riotProperties.matchUUIDUrl + puuid + "/ids?queue=" + matchPageable.queue + "&count=" + matchPageable.count)
            .retrieve()
            .bodyToMono(matchIds::class.java)
            .block()!!
    }

    override fun getMatch(matchId: String): MatchDto =
        webClient.mutate().build().get().uri(riotProperties.matchesMatchIdUrl + matchId)
            .retrieve()
            .bodyToMono(MatchDto().javaClass)
            .block()!!

}