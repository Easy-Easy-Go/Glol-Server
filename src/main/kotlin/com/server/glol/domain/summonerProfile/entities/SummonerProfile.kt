package com.server.glol.domain.summonerProfile.entities

import com.server.glol.domain.summonerProfile.dto.SummonerProfileDto
import com.server.glol.domain.summoner.entities.Summoner
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@DynamicUpdate
@Entity
class SummonerProfile(
    @Column(name = "queue_type")
    var queueType: String = "",

    @Column(name = "rank")
    var rank: String = "",

    var tier: String = "",

    @Column(name = "league_points")
    var leaguePoints: Int = 0,

    @Column(name = "wins")
    var wins: Int = 0,

    @Column(name = "losses")
    var losses: Int = 0,

    @Column(name = "league_id", unique = true)
    var leagueId: String = "",

    var winRate: Int = 0,

    var games: Int = 0,

    var rankScore: Int = 0,

    @ManyToOne(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY,
        optional = true
    )
    @JoinColumn(name = "summoner_idx")
    var summoner: Summoner? = null,

    @Column(name = "summonerProfile_idx")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long = 0
) {

    fun summonerProfileUpdate(summonerProfileDto: SummonerProfileDto, summoner: Summoner) {
        this.leagueId = summonerProfileDto.leagueId
        this.leaguePoints = summonerProfileDto.leaguePoints
        this.losses = summonerProfileDto.losses
        this.rank = summonerProfileDto.rank
        this.tier = summonerProfileDto.tier
        this.wins = summonerProfileDto.wins
        this.queueType = summonerProfileDto.queueType
        this.summoner = summoner
        this.winRate = summonerProfileDto.winRate
        this.games = summonerProfileDto.games
    }
}