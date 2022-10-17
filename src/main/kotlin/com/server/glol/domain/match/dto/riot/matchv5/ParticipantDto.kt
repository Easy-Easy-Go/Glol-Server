package com.server.glol.domain.match.dto.riot.matchv5

class ParticipantDto(
    val assists: Int,
    val champLevel: Int,
    val championId: Int,
    val championName: String,
    val deaths: Int,
    val kills: Int,
    val teamPosition: String,
    val teamId: Int,
    val win: Boolean,
    val item0: Int,
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val item6: Int,
    val summonerName: String,
    val wardsPlaced: Int,
    val wardsKilled: Int,
    val challenges: Challenges,
    val totalDamageDealtToChampions: Int,
    val totalMinionsKilled: Int
) {

}