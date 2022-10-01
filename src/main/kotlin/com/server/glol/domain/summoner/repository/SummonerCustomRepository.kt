package com.server.glol.domain.summoner.repository

import com.server.glol.domain.summoner.repository.projection.SummonerVo

interface SummonerCustomRepository {
    fun findSummonerByName(name: String) : SummonerVo?
}