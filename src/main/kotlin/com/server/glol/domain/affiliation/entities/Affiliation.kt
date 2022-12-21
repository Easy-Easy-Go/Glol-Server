package com.server.glol.domain.affiliation.entities

import com.server.glol.domain.group.entities.Group
import com.server.glol.domain.summoner.entities.Summoner
import javax.persistence.*

@Entity
class Affiliation(

    @ManyToOne(
        targetEntity = Group::class,
        fetch = FetchType.LAZY
    )
    @JoinColumn(nullable = true, updatable = true)
    var group: Group? = null,


    @ManyToOne(
        targetEntity = Summoner::class,
        fetch = FetchType.LAZY
    )
    @JoinColumn(nullable = true, updatable = true)
    var summoner: Summoner? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null
}
