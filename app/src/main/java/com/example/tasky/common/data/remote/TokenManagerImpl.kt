package com.example.tasky.common.data.remote

import android.util.Log
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.domain.TokenManager
import com.example.tasky.common.domain.repository.TokenRemoteRepository
import kotlinx.coroutines.flow.first
import java.time.ZonedDateTime

class TokenManagerImpl(
    private val tokenRemoteRepository: TokenRemoteRepository,
    private val sessionStateManager: SessionStateManager
) : TokenManager {
    companion object {
        private val TAG = "TokenManagerImpl"
    }

    override suspend fun isTokenExpired(): Boolean {
        val now = ZonedDateTime.now()
        val currentTimeInMilliseconds = now.toInstant().toEpochMilli()
        val expirationTime = sessionStateManager.getAccessTokenExpiration().first() ?: 0L
        return currentTimeInMilliseconds > expirationTime
    }

    override suspend fun refreshToken() {
        Log.d(TAG, "refreshToken()")
        val refreshToken = sessionStateManager.getRefreshToken() ?: ""
        val userId = sessionStateManager.getUserId().first() ?: ""

        val result = tokenRemoteRepository.refreshSession(
            refreshToken = refreshToken,
            userId = userId
        )

        when (result) {
            is Result.Success -> {
                Log.d(TAG, "Success refresh token!")
                sessionStateManager.setAccessToken(result.data.accessToken)
            }

            is Result.Error -> {
                Log.d(TAG, "Failed to refresh token :(")
                // Handle error properly
            }
        }
    }
}