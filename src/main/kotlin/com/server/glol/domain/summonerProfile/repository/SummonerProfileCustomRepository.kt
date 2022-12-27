package com.server.glol.domain.summonerProfile.repository

import com.server.glol.domain.summonerProfile.dto.projection.SummonerProfileResponse
import com.server.glol.domain.summonerProfile.entities.SummonerProfile

interface SummonerProfileCustomRepository {
    fun getSummonerProfileByName(name: String): MutableSet<SummonerProfileResponse>
    fun getSummonerProfile(name: String, queueType: String) : SummonerProfile?
}
