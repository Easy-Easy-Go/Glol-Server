package com.server.glol.domain.summonerProfile.service.facade

import com.server.glol.domain.summonerProfile.dto.SummonerProfileDto

interface RemoteSummonerProfileFacade {
    fun getSummonerProfile(summonerAccount: String) : MutableSet<SummonerProfileDto>
}