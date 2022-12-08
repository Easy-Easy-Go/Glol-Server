package com.server.glol.domain.match.repository

import com.server.glol.domain.match.entities.Champion
import org.springframework.data.jpa.repository.JpaRepository

interface ChampionRepository: JpaRepository<Champion, Long>