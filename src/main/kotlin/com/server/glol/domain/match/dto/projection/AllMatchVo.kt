package com.server.glol.domain.match.dto.projection

import com.querydsl.core.annotations.QueryProjection

data class AllMatchVo @QueryProjection constructor(
    val matchId: String,
    val gameDuration: Int,
    val queueId: String,
    val totalMinionsKilled: Int,
    val kills: Int,
    val assists: Int,
    val deaths: Int,
    val win: Boolean,
    val wardsPlaced: Int,
    val wardsKilled: Int,
    val controlWardsPlaced: Int,
    val firstSummonerSpell: Int,
    val secondSummonerSpell: Int,
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
    ) {

}