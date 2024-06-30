package com.example.tasky.common.domain

interface SessionStateManager {
    suspend fun setRefreshToken(refreshToken: String)
    fun getRefreshToken(): String?

    suspend fun setName(name: String)
    fun getName(): String?
}