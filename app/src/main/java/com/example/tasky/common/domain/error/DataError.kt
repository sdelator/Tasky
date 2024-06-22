package com.example.tasky.common.domain.error

sealed interface DataError : Error {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        UNAUTHORIZED,
        SERVER_ERROR,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL
    }

}

data class ApiErrorResponse(
    val message: String
)