package com.server.glol.domain.match.dto.projection

import com.querydsl.core.annotations.QueryProjection

class MetadataDao @QueryProjection constructor(
    val matchId: String,
    val queueId: String,
    val gameDuration: Int,
)  {
}