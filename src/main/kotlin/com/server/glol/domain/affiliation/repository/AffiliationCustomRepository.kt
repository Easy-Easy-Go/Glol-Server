package com.server.glol.domain.affiliation.repository

import com.server.glol.domain.affiliation.dto.AffiliationResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AffiliationCustomRepository {
    fun getAffiliationByTag(queueType: String?, teamName: String?, pageable: Pageable): Page<AffiliationResponse>
}