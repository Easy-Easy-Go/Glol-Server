package com.server.glol.domain.match.controller

import com.server.glol.domain.match.dto.MatchPageable
import com.server.glol.domain.match.service.MatchService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/match")
class MatchController(private val matchService: MatchService) {

    @PostMapping("/renewal/{name}")
    fun renewalMatches(
        @PathVariable name: String,
        @RequestParam("matchPageable") matchPageable: MatchPageable,
    ) = matchService.renewalMatches(name, matchPageable)

    @GetMapping("/{matchId}")
    fun getMatch(
        @PathVariable matchId: String,
    ) = matchService.getMatch(matchId)

        return matchService.getMatch(matchId)
    }

}