package com.server.glol.domain.league.controller

import com.server.glol.domain.league.service.LeagueService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/league")
class LeagueController(private val leagueService: LeagueService) {

    @GetMapping("/{name}")
    fun getLeague(@PathVariable name: String) = leagueService.getLeague(name)
}