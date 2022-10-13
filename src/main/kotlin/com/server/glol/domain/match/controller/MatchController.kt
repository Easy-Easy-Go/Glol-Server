package com.server.glol.domain.match.controller

import com.server.glol.domain.match.repository.projection.MatchResponse
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

    @PostMapping("/matches/by-name/{name}/Ids")
    fun getMatch(
        @PathVariable name: String,
        @RequestParam queue: Int,
        @RequestParam count: Int
    ) : MutableList<MatchResponse>? {

        return matchService.getMatches(name, queue, count)
    }

}