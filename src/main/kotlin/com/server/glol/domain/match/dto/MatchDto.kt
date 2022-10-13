package com.server.glol.domain.match.dto

class MatchDto(
        val metadata: MetadataDto,
        val info: InfoDto
) {
        constructor(): this(MetadataDto(), InfoDto())

}