package com.server.glol.domain.match.dto

data class MetadataTo(
    val matchId: String,
    val queueId: String,
    val gameDuration: Int,
) {
}