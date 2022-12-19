package com.server.glol.domain.league.dto.projection

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.querydsl.core.annotations.QueryProjection

@JsonIgnoreProperties(ignoreUnknown = true)
class LeagueResponse @QueryProjection constructor(
    val queueType: String = "",
    val tier: String = "",
    val rank: String = "",
    val summonerName: String = "",
    val leaguePoints: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
)