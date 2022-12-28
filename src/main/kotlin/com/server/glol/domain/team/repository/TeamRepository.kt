package com.server.glol.domain.team.repository

import com.server.glol.domain.team.entities.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : JpaRepository<Team, Long> {
    fun findByName(name: String): Team
    fun existsByName(name: String): Boolean
}