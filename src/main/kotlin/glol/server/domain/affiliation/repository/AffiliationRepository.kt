package com.server.glol.domain.affiliation.repository

import com.server.glol.domain.affiliation.entities.Affiliation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AffiliationRepository: JpaRepository<Affiliation, Long>