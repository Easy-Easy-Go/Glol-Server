package com.server.glol.domain.match.dto.projection

import com.querydsl.core.annotations.QueryProjection

data class AllMatchDto @QueryProjection constructor(
    val matchId: String = "",
    val gameDuration: Int = 0,
    val queueId: String = "",
    val totalMinionsKilled: Int = 0,
    val kills: Int = 0,
    val assists: Int = 0,
    val deaths: Int = 0,
    val win: Boolean = false,
    val wardsPlaced: Int = 0,
    val wardsKilled: Int = 0,
    val controlWardsPlaced: Int = 0,
    val firstSummonerSpell: Int = 0,
    val secondSummonerSpell: Int = 0,
    val item0: Int = 0,
    val item1: Int = 0,
    val item2: Int = 0,
    val item3: Int = 0,
    val item4: Int = 0,
    val item5: Int = 0,
    val item6: Int = 0,
    val championName: String = "",
    val championId: Int = 0,
    val championLevel: Int = 0,
    ) {

}