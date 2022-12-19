package com.server.glol.domain.match.repository

import com.server.glol.domain.match.entities.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository: JpaRepository<Item, Long>