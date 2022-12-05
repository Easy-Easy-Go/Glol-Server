package com.server.glol.domain.league.service.facade.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.server.glol.domain.league.dto.LeagueDto
import com.server.glol.domain.league.service.facade.RemoteLeagueFacade
import com.server.glol.global.config.properties.RiotProperties
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RemoteLeagueFacadeImpl(
    private val riotProperties: RiotProperties,
    private val webClient: WebClient,
) : RemoteLeagueFacade {
    override fun getLeague(summonerAccount: String): MutableSet<LeagueDto> {
        val leagueEntryDto: MutableSet<LeagueDto> = mutableSetOf()

        val mapper = ObjectMapper()
        return mapper.convertValue(webClient.mutate().build()
            .get().uri(riotProperties.leagueSummonerAccountUrl + summonerAccount)
            .retrieve().bodyToMono(leagueEntryDto::class.java).block(),
            object : TypeReference<MutableSet<LeagueDto>>() {})
    }
}