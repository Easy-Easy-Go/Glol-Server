package com.server.glol.domain.summonerProfile.dto.projection

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.querydsl.core.annotations.QueryProjection

@JsonIgnoreProperties(ignoreUnknown = true)
class SummonerProfileResponse @QueryProjection constructor(
    val queueType: String = "",
    val rank: String = "",
    val summonerName: String = "",
    val leaguePoints: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    var winRate: Int = 0,
    var games: Int = 0,
)