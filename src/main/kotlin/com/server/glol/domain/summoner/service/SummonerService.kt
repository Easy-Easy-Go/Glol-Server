package com.server.glol.domain.summoner.service

import com.server.glol.domain.summoner.repository.projection.SummonerVo

interface SummonerService {
    fun registerSummoner(name: String) : SummonerVo
    fun getSummoner(name: String): SummonerVo
    fun getPuuidByName(name: String) : String
}