package com.server.glol.domain.affiliation.entities

import com.server.glol.domain.summonerProfile.entities.SummonerProfile
import com.server.glol.domain.team.entities.Team
import javax.persistence.*

@Entity
class Affiliation(

    @ManyToOne(
        targetEntity = Team::class,
        fetch = FetchType.LAZY
    )
    @JoinColumn(nullable = true, updatable = true)
    var team: Team,


    @ManyToOne(
        targetEntity = SummonerProfile::class,
        fetch = FetchType.LAZY
    )
    @JoinColumn(nullable = true, updatable = true)
    var summonerProfile: SummonerProfile
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null
}
