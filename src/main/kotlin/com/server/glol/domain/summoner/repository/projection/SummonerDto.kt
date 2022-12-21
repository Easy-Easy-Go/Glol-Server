package com.server.glol.domain.summoner.repository.projection

import com.querydsl.core.annotations.QueryProjection
import com.server.glol.domain.summoner.entities.Summoner

class SummonerDto @QueryProjection constructor(
    val id: String = "",
    val accountId: String = "",
    val name: String = "",
    val puuid: String = "",
    val profileIconId: Int = 0,
) {

    fun toSummoner(): Summoner = Summoner(
        id = this.id,
        accountId = this.accountId,
        name = this.name,
        puuid = this.puuid,
        profileIconId = this.profileIconId
    )

}
