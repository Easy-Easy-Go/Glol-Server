package com.server.glol.domain.match.dto.riot.matchv5

data class MatchDto(
    val metadata: ParticipantsDto = ParticipantsDto(),
    val info: InfoDto = InfoDto()
)