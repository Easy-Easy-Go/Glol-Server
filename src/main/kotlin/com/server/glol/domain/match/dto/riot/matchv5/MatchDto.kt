package com.server.glol.domain.match.dto.riot.matchv5

class MatchDto(
    val metadata: MetadataDto,
    val info: InfoDto
) {
        constructor(): this(MetadataDto(), InfoDto())

}