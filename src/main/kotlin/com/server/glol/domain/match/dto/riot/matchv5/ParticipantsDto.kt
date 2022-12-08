package com.server.glol.domain.match.dto.riot.matchv5

data class ParticipantsDto(
    val matchId: String = "",
    val participants: MutableList<String> = mutableListOf()
)