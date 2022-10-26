package com.server.glol.domain.match.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator::class, property = "id")
@Entity
@Table(name = "item")
class Item(

    @Column(name = "item0")
        val item0: Int,

    @Column(name = "item1")
        val item1: Int,

    @Column(name = "item2")
        val item2: Int,

    @Column(name = "item3")
        val item3: Int,

    @Column(name = "item4")
        val item4: Int,

    @Column(name = "item5")
        val item5: Int,

    @Column(name = "item6")
        val item6: Int,

        @OneToOne(
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = [CascadeType.REMOVE],
        )
        @JoinColumn(name = "match_idx", nullable = true)
        val match: Match? = null,
) {
    @Column(name = "item_idx")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long = 0

    constructor(): this(0,0,0,0,0,0,0, null)
}