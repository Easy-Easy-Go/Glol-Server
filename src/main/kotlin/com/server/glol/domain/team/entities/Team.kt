package com.server.glol.domain.team.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Team(
    val name: String
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null
}