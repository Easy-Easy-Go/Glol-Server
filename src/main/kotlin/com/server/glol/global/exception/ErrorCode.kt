package com.server.glol.global.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

enum class ErrorCode(val status: HttpStatus, val msg: String) {
    // Summoner Exception
    ALREADY_EXISTS_SUMMONER(CONFLICT, "Already Exists Summoner"),
    NOT_FOUND_SUMMONER(NOT_FOUND, "Not Found Summoner"),

    // SummonerProfile Exception
    NOT_FOUND_SUMMONER_PROFILE(NOT_FOUND, "SummonerProfile does not Exists"),

    // Match Exception
    NOT_FOUND_MATCH(NOT_FOUND, "Not Found Match"),
    ALREADY_RENEWED(ACCEPTED, "Already Renewed"),

    // Affiliation Exception

    // Team Exception
    NOT_FOUND_TEAM(NOT_FOUND, "Not Found Team"),
    ALREADY_EXISTS_TEAM(CONFLICT, "Already Exists Team"),

    // Pageable Exception
    NOT_FOUND_SORT(NOT_FOUND, "Not found Sort"),;
}