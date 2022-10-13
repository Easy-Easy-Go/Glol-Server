package com.server.glol.domain.match.repository

import com.server.glol.domain.match.repository.projection.MatchResponse

interface MatchCustomRepository {
    fun findMatchIdBySummonerName(name: String) : MutableList<String>
    fun findMatchesByMatchIds(matchIds: MutableList<String>) : MutableList<MatchResponse>?
}