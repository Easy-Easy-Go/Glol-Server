package com.server.glol.domain.summonerProfile.service.facade.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.server.glol.domain.summonerProfile.dto.SummonerProfileDto
import com.server.glol.domain.summonerProfile.service.facade.RemoteSummonerProfileFacade
import com.server.glol.global.config.properties.RiotProperties
import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorCode.NOT_FOUND_SUMMONER_PROFILE
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class RemoteSummonerProfileFacadeImpl(
    private val webClient: WebClient,
) : RemoteSummonerProfileFacade {
    override fun getSummonerProfile(summonerAccount: String): MutableSet<SummonerProfileDto> {
        val summonerProfileDto: MutableSet<SummonerProfileDto> = mutableSetOf()

        val mapper = ObjectMapper()
        return mapper.convertValue(webClient.mutate().build()
            .get().uri(RiotProperties.SUMMONER_PROFILE_ACCOUNT_URL + summonerAccount)
            .retrieve().bodyToMono(summonerProfileDto::class.java).onErrorResume {
                throw CustomException(NOT_FOUND_SUMMONER_PROFILE)
            }.block(),
            object : TypeReference<MutableSet<SummonerProfileDto>>() {})
    }
}