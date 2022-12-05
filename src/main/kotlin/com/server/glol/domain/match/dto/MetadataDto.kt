package com.server.glol.domain.match.dto

data class MetadataDto(
    val matchId: String = "",
    val queueId: String = "",
    val gameDuration: Int = 0,
) {
}