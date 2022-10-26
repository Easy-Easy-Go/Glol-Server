package com.server.glol.domain.summoner.entities

import javax.persistence.*

@Entity
@Table(name = "summoner")
class Summoner(
    @Column(name = "id")
    val id: String,

    @Column(name = "account_id")
    val accountId: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "puuid")
    val puuid: String,

    @Column(name = "profile_icon_id")
    val profileIconId: Int,

    @Column(name = "visited")
    val visited: Boolean = false
) {
    @Id
    @Column(name = "summoner_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null

    @Column(name = "summoner_id")
    var summonerId: String = ""

    fun updateSummonerId(summonerId: String) {
        this.summonerId = summonerId
    }
}