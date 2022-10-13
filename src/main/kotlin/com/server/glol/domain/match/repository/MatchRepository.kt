package com.server.glol.domain.match.repository

import com.server.glol.domain.match.entities.Match
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MatchRepository: JpaRepository<Match, Long> {
    fun countByMatchId(matchId: String): Long
}