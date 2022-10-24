package com.server.glol.domain.summoner.service

import com.server.glol.domain.summoner.repository.projection.SummonerVo

interface SummonerServiceFacade {
    fun getPuuid(name: String): String
    fun getSummoner(name: String): SummonerVo
}