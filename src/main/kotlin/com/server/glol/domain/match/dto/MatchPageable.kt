package com.server.glol.domain.match.dto

data class MatchPageable(
    val queue: Int?,
    val type: String? = "",
    val count: Int
)