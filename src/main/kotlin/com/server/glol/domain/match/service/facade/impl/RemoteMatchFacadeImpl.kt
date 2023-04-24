package com.server.glol.domain.match.service.facade.impl

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto
import com.server.glol.domain.match.service.facade.RemoteMatchFacade
import com.server.glol.global.config.properties.RiotProperties
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class
RemoteMatchFacadeImpl(
    private val webClient: WebClient,
    private val riotProperties: RiotProperties
) : RemoteMatchFacade {

    val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun getMatchIds(puuid: String, matchPageable: MatchPageable): MutableList<String> {
        val matchIds: MutableList<String> = mutableListOf()

        return webClient.mutate().build()
            .get()
            .uri(riotProperties.matchesUUIDUrl + puuid + "/ids?count=" + matchPageable.count)
            .retrieve()
            .bodyToMono(matchIds::class.java)
            .onErrorResume {
                log.info("find puuid is : $puuid ${ErrorCode.NOT_FOUND_MATCH.msg}")
                throw CustomException(ErrorCode.NOT_FOUND_MATCH)
            }
            .block()!!
    }

    override fun getMatch(matchId: String): MatchDto =
        webClient.mutate().build()
            .get().uri(riotProperties.matchesByMatchIdUrl + matchId)
            .retrieve()
            .bodyToMono(MatchDto().javaClass)
            .onErrorResume {
                log.info("find matchId is : $matchId ${ErrorCode.NOT_FOUND_MATCH.msg}")
                throw CustomException(ErrorCode.NOT_FOUND_MATCH)
            }
            .block()!!
}