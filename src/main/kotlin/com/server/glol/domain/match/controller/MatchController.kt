package com.server.glol.domain.match.controller

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
    ) = matchService.renewalMatches(name, queue, count)

    @GetMapping("/{matchId}")
    fun getMatch(
        @PathVariable matchId: String,
    ) = matchService.getMatch(matchId)

        return matchService.getMatch(matchId)
    }

}