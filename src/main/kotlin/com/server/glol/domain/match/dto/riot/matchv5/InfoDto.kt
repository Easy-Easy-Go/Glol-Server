package com.server.glol.domain.match.dto.riot.matchv5

class InfoDto(
    val queueId: Int,
    val gameDuration: Int,
    val participants: MutableList<ParticipantDto>,
    val teams: MutableList<TeamDto>,
) {

    constructor(): this(0, 0, mutableListOf(), mutableListOf())

}
