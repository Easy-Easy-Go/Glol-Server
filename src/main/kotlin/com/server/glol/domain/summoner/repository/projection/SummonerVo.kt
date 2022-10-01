package com.server.glol.domain.summoner.repository.projection

import com.querydsl.core.annotations.QueryProjection

class SummonerVo @QueryProjection constructor(
    val id: String,
    val accountId: String,
    val name: String,
    val puuid: String,
    val profileIconId: Int,
    val visited: Boolean
) {

    constructor(): this("", "", "", "", 0, false)
}