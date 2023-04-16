package com.server.glol.domain.summonerProfile.repository

import com.server.glol.domain.summonerProfile.dto.projection.SummonerProfileResponse
import com.server.glol.domain.summonerProfile.entities.SummonerProfile

interface SummonerProfileCustomRepository {
    fun getSummonerProfileByName(name: String): MutableSet<SummonerProfileResponse>
    fun getSummonerProfileByQueueTypeAndName(name: String, queueType: String) : SummonerProfile?
    fun getHighRankSummonerProfileByName(name: String): SummonerProfile
}
