package com.server.glol.domain.league.entities

import com.server.glol.domain.league.dto.LeagueEntryDTO
import com.server.glol.domain.summoner.entities.Summoner
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Table(name = "league")
@DynamicUpdate
@Entity
class League(
    @Column(name = "queue_type")
    var queueType: String = "",

    @Column(name = "tier")
    var tier: String = "",

    @Column(name = "rank")
    var rank: String = "",

    @Column(name = "league_points")
    var leaguePoints: Int = 0,

    @Column(name = "wins")
    var wins: Int = 0,

    @Column(name = "losses")
    var losses: Int = 0,

    @Column(name = "league_id", unique = true)
    var leagueId: String = "",

    @ManyToOne(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY,
        optional = true
    )
    @JoinColumn(name = "summoner_idx")
    var summoner: Summoner? = null
) {

    @Column(name = "league_idx")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long = 0

    fun leagueUpdate(leagueDto: LeagueEntryDTO, summoner: Summoner) {
        this.leagueId = leagueDto.leagueId
        this.leaguePoints = leagueDto.leaguePoints
        this.losses = leagueDto.losses
        this.rank = leagueDto.rank
        this.tier = leagueDto.tier
        this.wins = leagueDto.wins
        this.queueType = leagueDto.queueType
        this.summoner = summoner
    }
}