package com.server.glol.domain.match.service

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.dto.riot.matchv5.MatchDto

interface MatchServiceFacade {
    fun getMatchIds(puuid: String, matchPageable: MatchPageable): MutableList<String>
    fun getMatch(matchId: String): MatchDto
}