package com.server.glol.domain.match.controller

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.service.MatchService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/match")
class MatchController(private val matchService: MatchService) {

    @PostMapping("/renewal/{name}")
    fun renewalMatches(
        @PathVariable name: String,
        matchPageable: MatchPageable,
    ) = matchService.renewalMatches(name, matchPageable)

    @GetMapping("/{matchId}")
    fun getMatch(
        @PathVariable matchId: String,
    ) = matchService.getMatch(matchId)

    @GetMapping("/matches/{name}")
    fun getMatches(
        @PathVariable name: String,
        @PageableDefault(page = 0, size = 20) pageable: Pageable,
        matchPageable: MatchPageable,
    ) = matchService.getMatches(name, matchPageable, pageable)

}