package com.example.tasky.common.domain.util

import android.util.Log
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.feature_agenda.domain.repository.AuthenticatedRemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class TokenManager(
    private val authenticatedRemoteRepository: AuthenticatedRemoteRepository,
    private val scope: CoroutineScope,
    private val sessionStateManager: SessionStateManager
) {
    companion object {
        private val TAG = "TokenManager"
    }

    fun shouldRefreshToken() {
        scope.launch {
            val now = ZonedDateTime.now()
            val currentTimeInMilliseconds = now.toInstant().toEpochMilli()

            val expirationTime = sessionStateManager.getAccessTokenExpiration().first() ?: 0L
            if (currentTimeInMilliseconds > expirationTime) {
                refreshToken()
            }
        }
    }

    private suspend fun refreshToken() {
        Log.d(TAG, "refreshToken()")
        val refreshToken = sessionStateManager.getRefreshToken() ?: ""
        val userId = sessionStateManager.getUserId().first()

        val result = authenticatedRemoteRepository.refreshSession(
            refreshToken = refreshToken,
            userId = userId.toString()
        )

        when (result) {
            is Result.Success -> {
                println("success refresh token!")
                sessionStateManager.setAccessToken(result.data.accessToken)
                // Emit a ViewState to update the UI
            }

            is Result.Error -> {
                println("failed refresh token :(")
                // Handle error properly
            }
        }
    }
}