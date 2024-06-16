package com.example.tasky.common.model

import android.content.Context
import com.example.tasky.R
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ResponseHandler @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun <T : Any> handleSuccess(data: T): Resource<T> {
        return Resource.Success(data)
    }

    fun <T : Any> handleFailure(e: Exception, errorMessage: String? = null): Resource.Failure<T> {
        return when (e) {
            is HttpException -> Resource.Failure(getErrorMessage(e.code(), errorMessage), null)
            else -> Resource.Failure(context.getString(R.string.content_failure), null)
        }
    }

    private fun getErrorMessage(
        code: Int,
        msg: String? = "unable to connect. Please try again"
    ): String {
        return context.getString(R.string.response_error, msg, code)
    }

}

