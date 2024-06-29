package com.example.tasky.common.domain

interface TaskyState {
    suspend fun setRefreshToken(refreshToken: String)
    suspend fun getRefreshToken(): String

    suspend fun setName(name: String)
    suspend fun getName(): String
}