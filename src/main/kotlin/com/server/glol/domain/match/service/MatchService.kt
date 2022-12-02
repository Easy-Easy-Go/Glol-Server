package com.server.glol.domain.match.service

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.dto.MatchResponse
import com.server.glol.domain.match.dto.projection.AllMatchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MatchService {
    fun renewalMatches(name: String, matchPageable: MatchPageable)
    fun getMatch(matchId: String): MatchResponse
    fun getMatches(name: String, matchPageable: MatchPageable, pageable: Pageable): Page<AllMatchDto>
}