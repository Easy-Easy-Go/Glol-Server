package com.server.glol.domain.match.dto.riot.matchv5

data class MetadataDto(
    val matchId: String = "",
    val participants: MutableList<String> = mutableListOf()
)