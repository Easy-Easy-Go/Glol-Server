package com.server.glol.domain.team.controller

import com.server.glol.domain.team.dto.RegisterTeamDto
import com.server.glol.domain.team.service.TeamService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/team")
class TeamController(private val teamService: TeamService) {

    @PostMapping("/register")
    fun createTeam(@RequestBody registerTeamDto: RegisterTeamDto) = teamService.createTeam(registerTeamDto)

    @GetMapping("/register/{teamName}")
    fun existsCheckTeam(@PathVariable teamName: String) = teamService.teamExistsCheck(teamName)
}