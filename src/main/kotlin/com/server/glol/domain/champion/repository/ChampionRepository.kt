package com.server.glol.domain.champion.repository

import com.server.glol.domain.champion.entities.Champion
import org.springframework.data.jpa.repository.JpaRepository

interface ChampionRepository: JpaRepository<Champion, Long>