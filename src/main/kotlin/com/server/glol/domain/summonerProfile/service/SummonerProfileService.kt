package com.server.glol.domain.summonerProfile.service

import com.server.glol.domain.summonerProfile.dto.projection.SummonerProfileResponse

interface SummonerProfileService {
    fun getSummonerProfile(name: String) : MutableSet<SummonerProfileResponse>
    fun saveSummonerProfile(name: String)
}