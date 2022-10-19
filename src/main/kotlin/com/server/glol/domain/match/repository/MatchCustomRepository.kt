package com.server.glol.domain.match.repository

import com.server.glol.domain.match.dto.MatchResponse

interface MatchCustomRepository {
    fun findMatchIdBySummonerName(name: String) : MutableList<String>
    fun findMatchesByMatchIds(matchId: String) : MatchResponse?
}