package com.server.glol.domain.affiliation.dto.projection

import com.querydsl.core.annotations.QueryProjection

class AffiliationDto @QueryProjection constructor(
    val name: String,
    val level: Int,
    val profileIcon: Int,
    val teamName: String,
    val queueType: String,
    val win: Int,
    val losses: Int,
    val winRate: Int,
    val games: Int,
    val rank: String,
    val tier: String
)