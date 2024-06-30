package com.example.tasky.common.domain

interface SessionStateManager {
    suspend fun setAccessToken(accessToken: String)
    fun getAccessToken(): String?
    suspend fun setRefreshToken(refreshToken: String)
    suspend fun getRefreshToken(): String

    suspend fun setName(name: String)
    suspend fun getName(): String
}