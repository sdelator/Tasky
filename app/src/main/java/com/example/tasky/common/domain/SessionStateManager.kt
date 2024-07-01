package com.example.tasky.common.domain

import kotlinx.coroutines.flow.Flow

interface SessionStateManager {
    suspend fun setAccessToken(accessToken: String)
    fun getAccessToken(): Flow<String?>
    suspend fun setRefreshToken(refreshToken: String)
    fun getRefreshToken(): String?

    suspend fun setName(name: String)
    fun getName(): String?
}