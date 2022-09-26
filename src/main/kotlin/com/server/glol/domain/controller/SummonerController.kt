package com.server.glol.domain.controller

import com.server.glol.domain.dto.SummonerResponseDto
import com.server.glol.domain.service.SummonerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/summoner")
class SummonerController(private val summonerService: SummonerService) {

    @GetMapping("/account/{name}")
    fun getOneSummonerAccount(@PathVariable name: String): SummonerResponseDto? {
        return summonerService.getSummonerAccount(name)
    }

}
