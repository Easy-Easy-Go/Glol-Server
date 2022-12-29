package com.server.glol.domain.item.repository

import com.server.glol.domain.item.entities.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository: JpaRepository<Item, Long>