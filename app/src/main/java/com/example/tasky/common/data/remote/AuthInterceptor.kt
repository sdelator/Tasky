package com.example.tasky.common.data.remote

import com.example.tasky.common.domain.SessionStateManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionStateManager: SessionStateManager
) : Interceptor {
    companion object {
        const val AUTH = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = sessionStateManager.getAccessToken()
        val request = chain.request().newBuilder()
            .addHeader(AUTH, "Bearer $accessToken")
            .build()
        return chain.proceed(request)
    }
}