package com.server.glol.domain.match.entities

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.server.glol.domain.match.dto.MatchDetailDto
import javax.persistence.*

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "id")
@Entity
@Table(name = "champion")
class Champion(

    @Column(name = "champion_name")
    val championName: String,

    @Column(name = "champion_id")
    val championId: Int,

    @Column(name = "champion_level")
    val championLevel: Int,

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
    )
    @JoinColumn(name = "match_idx", nullable = true)
    val match: Match? = null,
) {

    @Id
    @Column(name = "champion_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val championIdx: Long = 0

    constructor(matchDetailDto: MatchDetailDto, match: Match) : this(
        matchDetailDto.championName,
        matchDetailDto.championId,
        matchDetailDto.championLevel,
        match
    )
}