package com.example.tasky.feature_login.data.remote

import com.example.tasky.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class BaseHeaderInterceptor : Interceptor {
    companion object {
        const val API_KEY = "x-api-key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(API_KEY, BuildConfig.API_KEY)
            .build()
        return chain.proceed(request)
    }
}