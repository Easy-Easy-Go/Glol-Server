package com.server.glol.domain.match.dto.riot.matchv5

data class InfoDto(
    val queueId: Int = 0,
    val gameDuration: Int = 0,
    val participants: MutableList<ParticipantDto> = mutableListOf(),
    val teams: MutableList<TeamDto> = mutableListOf(),
)
