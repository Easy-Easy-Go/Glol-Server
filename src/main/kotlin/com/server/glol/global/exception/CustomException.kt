package com.server.glol.global.exception

class CustomException(val errorCode: ErrorCode) : RuntimeException() {
}