package com.server.glol.domain.match.dto

data class MatchInfoTo(
    val totalMinionsKilled: Int,
    val kills: Int,
    val assists: Int,
    val deaths: Int,
    val teamPosition: String,
    val teamId: Int,
    val win: Boolean,
    val wardsPlaced: Int,
    val wardsKilled: Int,
    val controlWardsPlaced: Int,
    val totalDamageDealtToChampions: Int,
    val item0: Int,
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val item6: Int,
    val championName: String,
    val championId: Int,
    val championLevel: Int,
    val name: String,
    val firstSummonerSpell: Int,
    val secondSummonerSpell: Int,
) {
}