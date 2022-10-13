package com.server.glol.domain.summoner.service

import com.server.glol.domain.summoner.repository.projection.SummonerVo

interface SummonerService {
    fun registrationSummoner(name: String) : SummonerVo
    fun getSummoner(name: String): SummonerVo
}