package com.server.glol.domain.match.dto.riot.matchv5.rune

data class PerkStyleDto(
    val description: String = "",
    val selections: MutableList<PerkStyleSelectionDto> = mutableListOf(),
    val style: Int = 0
)