package com.server.glol.domain.league.service

import com.server.glol.domain.league.dto.projection.LeagueResponse

interface LeagueService {
    fun getLeague(id: String) : MutableSet<LeagueResponse>
    fun saveLeague(name: String)
}