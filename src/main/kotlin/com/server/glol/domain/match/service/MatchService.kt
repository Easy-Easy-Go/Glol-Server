package com.server.glol.domain.match.service

import com.server.glol.domain.match.dto.MatchResponse

interface MatchService {
    fun renewalMatches(name: String, queue: Int, count: Int)
    fun getMatch(matchId: String): MatchResponse
}