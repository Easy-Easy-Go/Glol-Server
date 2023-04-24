package com.server.glol.global.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RiotProperties(

    @Value("\${riot.summoner.url}")
    val summonerUrl: String,

    @Value("\${riot.summoner.name.url}")
    val summonerByNameUrl: String,

    @Value("\${riot.summoner.puuid.url}")
    val summonerByPuuidUrl: String,

    @Value("\${riot.matches.uuid.url}")
    val matchesUUIDUrl: String,

    @Value("\${riot.matches.matchId.url}")
    val matchesByMatchIdUrl: String,

    @Value("\${riot.summoner.profile.url}")
    val summonerProfileUrl: String,

    @Value("\${riot.origin}")
    val origin: String,

    @Value("\${riot.secretKey}")
    var secretKey: String,
)