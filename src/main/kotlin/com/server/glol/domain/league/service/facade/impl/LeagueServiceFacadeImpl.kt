package com.server.glol.domain.league.service.facade.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.server.glol.domain.league.dto.LeagueEntryDTO
import com.server.glol.domain.league.service.facade.LeagueServiceFacade
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.StandardCharsets

@Service
class LeagueServiceFacadeImpl(
    private val riotProperties: RiotProperties,

    ) : LeagueServiceFacade {
    override fun getLeague(summonerAccount: String): MutableSet<LeagueEntryDTO> {
        val leagueEntryDto: MutableSet<LeagueEntryDTO> = mutableSetOf()

        val mapper = ObjectMapper()
        return mapper.convertValue(
            WebClient.create().get()
                .uri(riotProperties.leagueSummonerAccountUrl + summonerAccount)
                .headers { httpHeaders ->
                    httpHeaders.contentType = MediaType.APPLICATION_JSON
                    httpHeaders.acceptCharset = listOf(StandardCharsets.UTF_8)
                    httpHeaders.set("X-Riot-Token", riotProperties.secretKey)
                    httpHeaders.set("Origin", riotProperties.origin)
                }.retrieve().bodyToMono(leagueEntryDto::class.java).block()!!,
            object : TypeReference<MutableSet<LeagueEntryDTO>>() {})
    }
}