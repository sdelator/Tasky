package com.example.tasky.common.data.remote

import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.domain.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionStateManager: SessionStateManager,
    private val tokenManager: TokenManager
) : Interceptor {
    companion object {
        const val AUTH = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking {
            if (tokenManager.isTokenExpired()) {
                tokenManager.refreshToken()
            }
        }

        val accessToken = runBlocking {
            sessionStateManager.getAccessToken().first()
        }
        val request = chain.request().newBuilder()
            .addHeader(AUTH, "Bearer $accessToken")
            .build()
        return chain.proceed(request)
    }
}