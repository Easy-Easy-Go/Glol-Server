package com.server.glol.domain.group.repository

import com.server.glol.domain.group.entities.Group
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository: JpaRepository<Group, Long> {
    fun findByName(name: String): Group
}