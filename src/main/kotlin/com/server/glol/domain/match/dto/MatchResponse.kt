package com.server.glol.domain.match.dto

data class MatchResponse(
    val metaDataTo: MetadataTo,
    val matchInfo: MutableList<MatchInfoTo>,
) {
}