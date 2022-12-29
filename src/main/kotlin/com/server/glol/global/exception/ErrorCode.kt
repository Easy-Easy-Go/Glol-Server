package com.server.glol.global.exception

enum class ErrorCode(val status: Int, val msg: String) {

    // Summoner Exception
    ALREADY_EXISTS_SUMMONER(409, "Already Exists Summoner"),
    NOT_FOUND_SUMMONER(404, "Not Found Summoner"),

    // SummonerProfile Exception
    NOT_FOUND_SUMMONER_PROFILE(404, "SummonerProfile does not Exists"),

    // Match Exception
    NOT_FOUND_MATCH(404, "Not Found Match"),
    ALREADY_RENEWED(202, "Already Renewed"),

    // Affiliation Exception

    // Team Exception
    NOT_FOUND_TEAM(404, "Not Found Team"),
    ALREADY_EXISTS_TEAM(409, "Already Exists Team"),

    // Pageable Exception
    NOT_FOUND_SORT(404, "Not found Sort"),;


}