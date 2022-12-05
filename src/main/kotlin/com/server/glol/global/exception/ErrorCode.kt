package com.server.glol.global.exception

enum class ErrorCode(val status: Int, val msg: String) {
    NOT_FOUND_SUMMONER(404, "Not Found Summoner"),
    ALREADY_EXISTS_SUMMONER(404, "Already Exists Summoner"),
    NOT_FOUND_MATCH(404, "Not Found Match")
}