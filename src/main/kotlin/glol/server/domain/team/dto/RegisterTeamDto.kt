package com.server.glol.domain.team.dto

data class RegisterTeamDto(
    val teamName: String,
    val summonerProfileIdx: Long
)