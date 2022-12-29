package com.server.glol.domain.summoner.service.facade

import com.server.glol.domain.summoner.dto.projection.SummonerDto

interface RemoteSummonerFacade {
    fun getSummonerByName(name: String): SummonerDto
    fun getSummonerByPuuid(puuid: String): SummonerDto
}