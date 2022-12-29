package com.server.glol.domain.summoner.controller

import com.server.glol.domain.summoner.service.SummonerService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/summoner")
class SummonerController(private val summonerService: SummonerService) {

    @PostMapping("/registration/{name}")
    fun registrationSummoner(@PathVariable name: String): Long = summonerService.registerSummoner(name)
}
