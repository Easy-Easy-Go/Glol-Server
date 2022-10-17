package com.server.glol.domain.match.dto

class MatchResponse(
    val metaDataTo: MetadataTo,
    val matchInfo: MutableList<MatchInfoTo>,
) {
}