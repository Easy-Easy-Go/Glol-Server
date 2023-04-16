package com.server.glol.global.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

@Component
class RiotProperties (
    @Value("\${summoner.url}")
    var summonerBaseUrl: String,

    @Value("\${summoner.name.url")
    var summonerNameUrl: String,

    @Value("\${summoner.puuid.url}")
    var summonerByPuuidAPIURL: String,

    @Value("\${match.uuid.url}")
    var matchUUIDUrl: String,

    @Value("\${matches.matchId.url}")
    var matchesMatchIdUrl: String,

    @Value("\${summoner-profile.url}")
    var summonerProfileAccountUrl: String,

    @Value("\${origin}")
    var origin: String,

    @Value("\${secretKey}")
    var secretKey: String
)