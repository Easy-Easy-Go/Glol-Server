package com.server.glol.domain.match.entities

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@Table(name = "champion")
class Champion(

    @Column(name = "champion_name")
    val championName: String,

    @Column(name = "champion_id")
    val championId: Int,

    @Column(name = "champion_level")
    val championLevel: Int,

    @JsonIgnore
    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
    )
    @JoinColumn(name = "match_idx", nullable = true)
    val match: Match? = null,
) {

    @Column(name = "champion_idx")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val championIdx: Long = 0

}