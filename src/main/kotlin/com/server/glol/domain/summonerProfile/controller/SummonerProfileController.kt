package com.server.glol.domain.summonerProfile.controller

import com.server.glol.domain.summonerProfile.service.SummonerProfileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/summonerProfile")
class SummonerProfileController(private val summonerProfileService: SummonerProfileService) {

    @GetMapping("/{name}")
    fun getLeague(@PathVariable name: String) = summonerProfileService.getSummonerProfile(name)

    @PostMapping("/{name}")
    fun saveLeague(@PathVariable name: String) = summonerProfileService.saveSummonerProfile(name)
}