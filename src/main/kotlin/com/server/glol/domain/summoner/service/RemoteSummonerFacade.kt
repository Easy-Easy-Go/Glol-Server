package com.server.glol.domain.summoner.service

import com.server.glol.domain.summoner.repository.projection.SummonerDto

interface RemoteSummonerFacade {
    fun getSummonerByName(name: String): SummonerDto
    fun getSummonerByPuuid(puuid: String): SummonerDto
}