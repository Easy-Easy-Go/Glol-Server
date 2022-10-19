package com.server.glol.domain.match.controller

import com.server.glol.domain.match.dto.MatchResponse
import com.server.glol.domain.match.service.MatchService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/match")
class MatchController(val matchService: MatchService) {

    @PostMapping("/renewal/{name}")
    fun renewalMatches(
            @PathVariable name: String,
            @RequestParam queue: Int,
            @RequestParam count: Int
            ) {

        return matchService.renewalMatches(name, queue, count)
    }

    @GetMapping("/{matchId}")
    fun getMatch(
        @PathVariable matchId: String,
    ) : MatchResponse {

        return matchService.getMatch(matchId)
    }

}