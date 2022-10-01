package com.server.glol.domain.summoner.repository

import com.server.glol.domain.summoner.entites.Summoner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SummonerRepository : JpaRepository<Summoner, String> {

}