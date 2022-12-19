package com.server.glol.domain.league.repository

import com.server.glol.domain.league.dto.projection.LeagueResponse
import com.server.glol.domain.league.entities.League

interface LeagueCustomRepository {
    fun getLeagueEntryByName(name: String): MutableSet<LeagueResponse>
    fun getLeague(name: String, queueType: String) : League?
}
