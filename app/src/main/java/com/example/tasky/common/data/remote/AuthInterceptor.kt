package com.example.tasky.common.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    companion object {
        const val AUTH = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = "" // todo get from sharedPref
        val request = chain.request().newBuilder()
            .addHeader(AUTH, "Bearer $accessToken")
            .build()
        return chain.proceed(request)
    }
}