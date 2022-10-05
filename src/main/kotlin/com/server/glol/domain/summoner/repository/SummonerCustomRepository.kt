package com.server.glol.domain.summoner.repository

import com.server.glol.domain.summoner.entites.Summoner
import com.server.glol.domain.summoner.repository.projection.SummonerVo

interface SummonerCustomRepository {
    fun findSummonerByName(name: String) : SummonerVo?
    fun findPuuidByName(name: String) : Summoner?
}