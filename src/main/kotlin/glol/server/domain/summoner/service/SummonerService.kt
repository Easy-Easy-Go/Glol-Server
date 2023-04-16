package com.server.glol.domain.summoner.service

interface SummonerService {
    fun registerSummoner(name: String): Long
    fun getPuuid(name: String): String
}