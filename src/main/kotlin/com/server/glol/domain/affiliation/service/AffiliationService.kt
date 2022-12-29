package com.server.glol.domain.affiliation.service

import com.server.glol.domain.affiliation.dto.SaveAffiliationDto
import com.server.glol.domain.affiliation.dto.AffiliationResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AffiliationService {
    fun getAffiliationByTag(
        queueType: String?,
        teamName: String?,
        pageable: Pageable
    ): Page<AffiliationResponse>

    fun saveAffiliation(saveAffiliationDto: SaveAffiliationDto)
}