package com.server.glol.domain.league.entities

import com.server.glol.domain.summoner.entities.Summoner
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Table(name = "league")
@DynamicUpdate
@Entity
class League(
    @Column(name = "queue_type")
    val queueType: String,

    @Column(name = "tier")
    val tier: String,

    @Column(name = "rank")
    val rank: String,

    @Column(name = "league_points")
    val leaguePoints: Int,

    @Column(name = "wins")
    val wins: Int,

    @Column(name = "losses")
    val losses: Int,

    @Column(name = "league_id")
    val leagueId: String,

    @ManyToOne(
        cascade = [CascadeType.REMOVE],
        fetch = FetchType.LAZY,
        optional = true
    )
    @JoinColumn(name = "summoner_idx")
    val summoner: Summoner? = null
) {

    @Column(name = "league_idx")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long = 0
}