package com.server.glol.domain.summoner.repository.projection

import com.querydsl.core.annotations.QueryProjection

class SummonerDto @QueryProjection constructor(
    val id: String = "",
    val accountId: String = "",
    val name: String = "",
    val puuid: String = "",
    val profileIconId: Int = 0,
)