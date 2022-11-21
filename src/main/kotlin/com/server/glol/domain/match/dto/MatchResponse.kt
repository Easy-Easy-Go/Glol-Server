package com.server.glol.domain.match.dto

data class MatchResponse(
    val metaDataDto: MetadataDto,
    val matchInfo: MutableList<MatchInfoDto>,
) {
}