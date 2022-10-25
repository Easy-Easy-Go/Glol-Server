package com.server.glol.domain.league.service.facade

import com.server.glol.domain.league.dto.LeagueEntryDTO

interface LeagueServiceFacade {
    fun getLeague(summonerAccount: String) : MutableSet<LeagueEntryDTO>
}