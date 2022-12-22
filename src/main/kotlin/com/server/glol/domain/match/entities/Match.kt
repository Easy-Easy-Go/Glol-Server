package com.server.glol.domain.match.entities

import com.server.glol.domain.champion.entities.Champion
import com.server.glol.domain.item.entities.Item
import com.server.glol.domain.match.dto.MatchDetailDto
import com.server.glol.domain.summoner.entities.Summoner
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "match")
class Match(

    val matchId: String?,

    val kills: Int?,

    val assists: Int?,

    val deaths: Int?,

    val teamPosition: String?,

    val teamId: Int?,

    val win: Boolean?,

    val wardsPlaced: Int?,

    val wardsKilled: Int?,

    val controlWardsPlaced: Int?,

    val totalDamageDealtToChampions: Int?,

    val totalMinionsKilled: Int,

    val queueType: String?,

    val gameDuration: Int?,

    val createdAt: LocalDateTime?,

    val firstSummonerSpell: Int?,

    val secondSummonerSpell: Int?,

    val gameCreation: Long?,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
    )
    @JoinColumn(name = "summoner_idx", nullable = true)
    var summoner: Summoner? = null,

    @OneToOne(
        mappedBy = "match"
    )
    val item: Item? = null,

    @OneToOne(
        mappedBy = "match",
    )
    val champion: Champion? = null,
) {

    @Column(name = "match_idx")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long = 0

    constructor(matchDetailDto: MatchDetailDto, summoner: Summoner) : this(
        matchId = matchDetailDto.matchId,
        kills = matchDetailDto.kills,
        assists = matchDetailDto.assists,
        deaths = matchDetailDto.deaths,
        teamPosition = matchDetailDto.teamPosition,
        teamId = matchDetailDto.teamId,
        win = matchDetailDto.win,
        wardsPlaced = matchDetailDto.wardsPlaced,
        wardsKilled = matchDetailDto.wardsKilled,
        controlWardsPlaced = matchDetailDto.controlWardsPlaced,
        createdAt = LocalDateTime.now(),
        totalDamageDealtToChampions = matchDetailDto.totalDamageDealtToChampions,
        totalMinionsKilled = matchDetailDto.totalMinionsKilled,
        summoner = summoner,
        queueType = matchDetailDto.queueId,
        gameDuration = matchDetailDto.gameDuration,
        gameCreation = matchDetailDto.gameCreation,
        firstSummonerSpell = matchDetailDto.summoner1Id,
        secondSummonerSpell = matchDetailDto.summoner2Id,
    )
}