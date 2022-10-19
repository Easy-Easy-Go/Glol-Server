package com.server.glol.domain.match.repository

import com.server.glol.domain.match.entities.Perk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PerkRepository: JpaRepository<Perk, Long>