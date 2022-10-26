package com.server.glol.domain.match.entities

import com.server.glol.domain.summoner.entites.Summoner
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "match")
class Match(

    @Column(name = "match_id")
    val matchId: String?,

    @Column(name = "kills")
    val kills: Int?,

    @Column(name = "assists")
    val assists: Int?,

    @Column(name = "deaths")
    val deaths: Int?,

    @Column(name = "team_position")
    val teamPosition: String?,

    @Column(name = "team_id")
    val teamId: Int?,

    @Column(name = "win")
    val win: Boolean?,

    @Column(name = "wards_placed")
    val wardsPlaced: Int?,

    @Column(name = "wards_killed")
    val wardsKilled: Int?,

    @Column(name = "control_ward_placed")
    val controlWardsPlaced: Int?,

    @Column(name = "total_damage_dealt_to_champions")
    val totalDamageDealtToChampions: Int?,

    @Column(name = "total_minions_killed")
    val totalMinionsKilled: Int,

    @Column(name = "queue_type")
    val queueType: String?,

    @Column(name = "game_duration")
    val gameDuration: Int?,

    @Column(name = "created_at")
    val createdAt: LocalDateTime?,

    @Column(name = "first_summoenr_spell")
    val firstSummonerSpell: Int?,

    @Column(name = "second_summoenr_spell")
    val secondSummonerSpell: Int?,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
    )
    @JoinColumn(name = "summoner_idx", nullable = true)
    val summoner: Summoner,

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
}