package com.server.glol.global.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RiotProperties(
    @Value("\${riot.secretKey}")
    val secretKey: String,

    @Value("\${riot.summoner.name.uri}")
    val summonerAPIUrl: String,

    @Value("\${riot.origin}")
    val origin: String
) {
}