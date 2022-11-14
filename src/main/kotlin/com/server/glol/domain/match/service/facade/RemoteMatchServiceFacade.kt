package com.server.glol.domain.match.service.facade

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto

interface RemoteMatchServiceFacade {
    fun getMatchIds(puuid: String, matchPageable: MatchPageable): MutableList<String>
    fun getMatch(matchId: String): MatchDto
}