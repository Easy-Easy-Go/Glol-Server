package com.server.glol.domain.summoner.service

interface SummonerService {
    fun registerSummonerByName(name: String)
    fun getPuuid(name: String): String
}