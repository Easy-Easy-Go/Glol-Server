package com.server.glol.domain.summoner.service

import com.server.glol.domain.summoner.repository.projection.SummonerDto

interface SummonerServiceFacade {
    fun getSummonerByName(name: String): SummonerDto
    fun getSummonerByPuuid(puuid: String): SummonerDto
}