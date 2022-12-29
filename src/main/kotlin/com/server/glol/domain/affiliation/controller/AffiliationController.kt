package com.server.glol.domain.affiliation.controller

import com.server.glol.domain.affiliation.dto.AffiliationResponse
import com.server.glol.domain.affiliation.dto.SaveAffiliationDto
import com.server.glol.domain.affiliation.service.AffiliationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/affiliation")
class AffiliationController(private val affiliationService: AffiliationService) {

    @GetMapping("/tag")
    fun getAffiliationByTag(
        @PathVariable queueType: String?,
        @PathVariable teamName: String?,
        @PageableDefault(size = 20) pageable: Pageable
    ): Page<AffiliationResponse> =
        affiliationService.getAffiliationByTag(queueType = queueType, teamName = teamName, pageable = pageable)

    @PostMapping("/save")
    fun saveAffiliation(@RequestBody saveAffiliationDto: SaveAffiliationDto) =
        affiliationService.saveAffiliation(saveAffiliationDto)
}