package com.server.glol.domain.affiliation.dto

import com.server.glol.domain.affiliation.dto.projection.AffiliationDto

class AffiliationResponse constructor(
    val name: String,
    val level: Int,
    val profileIcon: Int,
    val teamName: String,
    val queueType: String,
    val win: Int,
    val losses: Int,
    val winRate: Int,
    val games: Int,
    val rank: String,
) {
    constructor(affiliationDto: AffiliationDto): this(
        name = affiliationDto.name,
        level = affiliationDto.level,
        profileIcon = affiliationDto.profileIcon,
        teamName = affiliationDto.teamName,
        queueType = affiliationDto.queueType,
        win = affiliationDto.win,
        losses = affiliationDto.losses,
        winRate = affiliationDto.winRate,
        games = affiliationDto.games,
        rank = affiliationDto.tier + " ${affiliationDto.rank}"
    )
}