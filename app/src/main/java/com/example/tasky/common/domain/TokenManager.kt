package com.example.tasky.common.domain

interface TokenManager {
    suspend fun isTokenExpired(): Boolean
    suspend fun refreshToken()
}