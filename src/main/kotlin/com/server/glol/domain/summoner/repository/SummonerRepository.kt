package com.server.glol.domain.summoner.repository

import com.server.glol.domain.summoner.entities.Summoner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SummonerRepository : JpaRepository<Summoner, String> {
    fun findSummonerByName(name: String) : Summoner?
    fun existsSummonerByName(name: String) : Boolean

}