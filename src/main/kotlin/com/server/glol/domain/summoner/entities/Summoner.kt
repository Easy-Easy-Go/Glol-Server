package com.server.glol.domain.summoner.entities

import com.server.glol.domain.summoner.repository.projection.SummonerDto
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity
@DynamicUpdate
@Table(name = "summoner")
class Summoner(
    @Column(name = "id")
    val id: String = "",

    @Column(name = "account_id", unique = true)
    val accountId: String = "",

    @Column(name = "name", unique = true)
    val name: String = "",

    @Column(name = "puuid", unique = true)
    val puuid: String = "",

    @Column(name = "profile_icon_id")
    val profileIconId: Int = 0,

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


    constructor(summonerDto: SummonerDto) : this(
        id = summonerDto.id,
        accountId = summonerDto.accountId,
        name = summonerDto.name,
        puuid = summonerDto.puuid,
        profileIconId = summonerDto.profileIconId
    )
}