package com.example.tasky.common.data.util

import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class APIUtilFunctions @Inject constructor() {
    fun getHttpErrorMessage(e: HttpException): DataError.Network {
        val error = when (e.code()) {
            401 -> DataError.Network.UNAUTHORIZED
            408 -> DataError.Network.REQUEST_TIMEOUT
            413 -> DataError.Network.PAYLOAD_TOO_LARGE
            else -> DataError.Network.UNKNOWN
        }
        return error
    }
}