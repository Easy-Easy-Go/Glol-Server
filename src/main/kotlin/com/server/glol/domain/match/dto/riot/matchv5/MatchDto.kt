package com.server.glol.domain.match.dto.riot.matchv5

data class MatchDto(
    val metadata: MetadataDto = MetadataDto(),
    val info: InfoDto = InfoDto()
)