package com.server.glol.domain.league.repository

import com.server.glol.domain.league.entities.League
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeagueRepository : JpaRepository<League, Long> {
    fun findLeagueBySummonerNameAndQueueType(name: String, queueType: String): League?
}