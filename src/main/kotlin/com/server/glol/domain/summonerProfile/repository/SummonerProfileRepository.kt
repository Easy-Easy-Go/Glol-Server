package com.server.glol.domain.summonerProfile.repository

import com.server.glol.domain.summonerProfile.entities.SummonerProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SummonerProfileRepository : JpaRepository<SummonerProfile, Long> {
    fun existsBySummonerName(name: String): Boolean
    fun existsByIdx(idx: Long): Boolean
}