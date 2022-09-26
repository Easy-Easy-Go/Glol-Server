package com.server.glol.domain.service

import com.server.glol.domain.dto.SummonerResponseDto

interface SummonerService {
    fun getSummonerAccount(name: String): SummonerResponseDto?
}