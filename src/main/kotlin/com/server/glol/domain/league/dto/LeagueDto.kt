package com.server.glol.domain.league.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class LeagueDto(
    val leagueId: String = "",
    val queueType: String = "",
    val tier: String = "",
    val rank: String = "",
    val summonerId: String = "",
    val summonerName: String = "",
    val leaguePoints: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
)