package com.example.tasky.common.domain.util

sealed interface DataError : Error {
    sealed class Network(val code: Int, private val message: String = "") : DataError {
        fun formatErrorMessage(): String {
            return "${this.code}: ${this.message}"
        }

        class RequestTimeout(message: String = "Request Timeout") : Network(408, message)
        class Unauthorized(message: String = "Unauthorized") : Network(401, message)
        class PayloadTooLarge(message: String = "Payload Too Large") : Network(413, message)
        class Unknown(message: String = "Unknown Error") : Network(-2, message)
        class NoInternet(message: String = "No Internet") : Network(0, message)
    }

    enum class Local : DataError {
        DISK_FULL
    }

}

data class ApiErrorResponse(
    val message: String
)