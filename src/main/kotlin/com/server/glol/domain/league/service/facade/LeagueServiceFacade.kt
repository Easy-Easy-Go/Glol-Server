package com.server.glol.domain.league.service.facade

import com.server.glol.domain.league.dto.LeagueDto

interface LeagueServiceFacade {
    fun getLeague(summonerAccount: String) : MutableSet<LeagueDto>
}