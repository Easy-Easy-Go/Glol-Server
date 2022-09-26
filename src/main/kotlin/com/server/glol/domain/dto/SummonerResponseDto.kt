package com.server.glol.domain.dto

class SummonerResponseDto constructor (
    val name: String,
    val accountId: String,
    val summonerLevel: Int,
    val profileIconId: Int
) {

    constructor() : this("", "", 0, 0)
}