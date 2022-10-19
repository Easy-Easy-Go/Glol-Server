package com.server.glol.domain.match.entities

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "id")
@Entity
@Table(name = "perk")
class Perk(
    @Column(name = "rune")
    val rune: Int,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
    )
    @JoinColumn(name = "match_idx", nullable = false)
    val match: Match
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null
}