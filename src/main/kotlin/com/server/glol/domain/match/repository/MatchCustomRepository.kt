package com.server.glol.domain.match.repository

import com.server.glol.domain.match.dto.MatchResponse
import com.server.glol.domain.match.dto.projection.MatchesDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MatchCustomRepository {
    fun findMatchIdBySummonerName(name: String): MutableList<String>
    fun findMatchesByMatchIds(matchId: String): MatchResponse?
    fun findAllByMatchIds(name: String, matchIds: MutableList<String>, pageable: Pageable): Page<MatchesDto>
    fun findLastMatchIdBySummonerName(name: String): String?
}