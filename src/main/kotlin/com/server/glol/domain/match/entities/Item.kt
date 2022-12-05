package com.server.glol.domain.match.entities

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.server.glol.domain.match.dto.MatchDetailDto
import javax.persistence.*

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "id")
@Entity
@Table(name = "item")
class Item(

    @Column(name = "item0")
    val item0: Int = 0,

    @Column(name = "item1")
    val item1: Int = 0,

    @Column(name = "item2")
    val item2: Int = 0,

    @Column(name = "item3")
    val item3: Int = 0,

    @Column(name = "item4")
    val item4: Int = 0,

    @Column(name = "item5")
    val item5: Int = 0,

    @Column(name = "item6")
    val item6: Int = 0,

    @OneToOne(
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.REMOVE],
    )
    @JoinColumn(name = "match_idx", nullable = true)
    val match: Match? = null,
) {
    @Column(name = "item_idx")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long = 0

    constructor(matchDetailDto: MatchDetailDto, match: Match) : this(
        matchDetailDto.item0,
        matchDetailDto.item1,
        matchDetailDto.item2,
        matchDetailDto.item3,
        matchDetailDto.item4,
        matchDetailDto.item5,
        matchDetailDto.item6,
        match
    )
}