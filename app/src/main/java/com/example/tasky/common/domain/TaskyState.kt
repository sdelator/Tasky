package com.example.tasky.common.domain

interface TaskyState {
    suspend fun setRefreshToken(refreshToken: String)
    suspend fun getRefreshToken(): String
}