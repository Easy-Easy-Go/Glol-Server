package com.server.glol.domain.summonerProfile.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class SummonerProfileDto(
    val leagueId: String = "",
    var queueType: String = "",
    val tier: String = "",
    val rank: String = "",
    val summonerId: String = "",
    val summonerName: String = "",
    val leaguePoints: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    var winRate: Int = 0,
    var games: Int = 0,
    var rankScore: Int = 0
)