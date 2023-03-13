package com.server.glol.global.exception.controller

import com.server.glol.global.exception.CustomException
import com.server.glol.global.exception.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(CustomException::class)
    protected fun handleBaseException(exception: CustomException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(exception.errorCode.status.value())
            .body(ErrorResponse(exception.errorCode.status.value(), exception.errorCode.msg))
    }
}