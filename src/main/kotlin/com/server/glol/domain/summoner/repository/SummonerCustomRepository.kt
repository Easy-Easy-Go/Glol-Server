package com.server.glol.domain.summoner.repository

import com.server.glol.domain.summoner.repository.projection.SummonerDto

interface SummonerCustomRepository {
    fun findSummonerByName(name: String) : SummonerDto?
    fun findPuuidByName(name: String) : String?
    fun findIdByName(name: String) : String?
}