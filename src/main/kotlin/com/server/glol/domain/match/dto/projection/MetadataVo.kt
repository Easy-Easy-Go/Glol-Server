package com.server.glol.domain.match.dto.projection

import com.querydsl.core.annotations.QueryProjection

data class MetadataVo @QueryProjection constructor(
    val matchId: String,
    val queueId: String,
    val gameDuration: Int,
)