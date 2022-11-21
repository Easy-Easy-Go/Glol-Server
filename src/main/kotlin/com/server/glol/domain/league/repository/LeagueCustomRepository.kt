package com.server.glol.domain.league.repository

import com.server.glol.domain.league.dto.LeagueDto
import com.server.glol.domain.league.entities.League

interface LeagueCustomRepository {
    fun getLeagueEntryByName(name: String): MutableSet<LeagueDto>
    fun getLeague(name: String, queueType: String) : League?
}
