package com.server.glol.domain.summoner.controller

import com.server.glol.domain.summoner.service.SummonerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/summoner")
class SummonerController(private val summonerService: SummonerService) {

    @GetMapping("/registration/{name}")
    fun registrationSummoner(@PathVariable name: String) {
        summonerService.registrationSummoner(name)
    }

}
