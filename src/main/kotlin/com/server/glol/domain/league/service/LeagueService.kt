package com.server.glol.domain.league.service

import com.server.glol.domain.league.dto.LeagueDto

interface LeagueService {
    fun getLeague(name: String) : MutableSet<LeagueDto>
    fun saveLeague(name: String, leagueDto: MutableSet<LeagueDto>)
}