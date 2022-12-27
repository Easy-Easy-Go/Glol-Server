package com.server.glol.domain.team.service

import com.server.glol.domain.team.dto.RegisterTeamDto

interface TeamService {
    fun createTeam(dto: RegisterTeamDto)
    fun teamExistsCheck(teamName: String): Boolean
}