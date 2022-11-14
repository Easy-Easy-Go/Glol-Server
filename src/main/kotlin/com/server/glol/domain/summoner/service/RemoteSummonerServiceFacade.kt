package com.server.glol.domain.summoner.service

import com.server.glol.domain.summoner.repository.projection.SummonerVo

interface RemoteSummonerServiceFacade {
    fun getPuuid(name: String): String
    fun getSummoner(name: String): SummonerVo
}