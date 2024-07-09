package com.example.tasky.common.domain

import android.util.Log
import com.example.tasky.common.domain.repository.TokenRemoteRepository
import kotlinx.coroutines.flow.first
import java.time.ZonedDateTime
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val tokenRepository: TokenRemoteRepository,
    private val sessionStateManager: SessionStateManager
) {
    companion object {
        private val TAG = "TokenManager"
    }

    suspend fun shouldRefreshToken(): Boolean {
        val now = ZonedDateTime.now()
        val currentTimeInMilliseconds = now.toInstant().toEpochMilli()
        val expirationTime = sessionStateManager.getAccessTokenExpiration().first() ?: 0L

        return currentTimeInMilliseconds > expirationTime
    }

    suspend fun refreshToken() {
        Log.d(TAG, "refreshToken()")
        val refreshToken = sessionStateManager.getRefreshToken() ?: ""
        val userId = sessionStateManager.getUserId().first()

        val result = tokenRepository.refreshSession(refreshToken, userId.toString())

        when (result) {
            is Result.Success -> {
                println("Success refresh token!")
                sessionStateManager.setAccessToken(result.data.accessToken)
                sessionStateManager.setAccessTokenExpiration(result.data.expirationTimestamp)
            }

            is Result.Error -> {
                println("Failed refresh token :(")
            }
        }
    }
}