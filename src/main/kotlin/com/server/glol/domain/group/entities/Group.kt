package com.server.glol.domain.group.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Group(
    val name: String
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long? = null
}