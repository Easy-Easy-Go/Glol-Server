package com.server.glol.global.exception

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ErrorResponse(
    val status: Int = 0,
    val msg: String? = null,
) {
    val formatNow: String =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd / HH : mm : ss "))
}