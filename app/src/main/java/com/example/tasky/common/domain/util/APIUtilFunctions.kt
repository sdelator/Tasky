package com.example.tasky.common.domain.util

import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class APIUtilFunctions @Inject constructor() {
    fun parseErrorResponse(errorBody: String?): String {
        return if (errorBody != null) {
            try {
                val apiErrorResponse = Gson().fromJson(errorBody, ApiErrorResponse::class.java)
                apiErrorResponse.message
            } catch (e: Exception) {
                "$e"
            }
        } else {
            "Unknown error"
        }
    }

    fun getHttpErrorMessage(e: HttpException): DataError.Network {
        val errorMessage = e.message()
        val error = when (e.code()) {
            401 -> DataError.Network.Unauthorized(errorMessage)
            408 -> DataError.Network.RequestTimeout(errorMessage)
            413 -> DataError.Network.PayloadTooLarge(errorMessage)
            else -> DataError.Network.Unknown(errorMessage)
        }
        return error
    }
}