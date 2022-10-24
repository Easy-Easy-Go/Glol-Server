package com.server.glol.domain.match.service

import com.server.glol.domain.match.dto.MatchResponse

interface MatchService {
    fun renewalMatches(name: String, matchPageable: MatchPageable)
    fun getMatch(matchId: String): MatchResponse
}