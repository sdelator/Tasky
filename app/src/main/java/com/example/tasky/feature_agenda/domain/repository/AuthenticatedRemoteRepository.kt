package com.example.tasky.feature_agenda.domain.repository

import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.model.AccessTokenResponse

interface AuthenticatedRemoteRepository {
    suspend fun logOutUser(): Result<Unit, DataError.Network>
    suspend fun refreshSession(
        refreshToken: String,
        userId: String
    ): Result<AccessTokenResponse, DataError.Network>

    suspend fun checkAuthentication(): Result<Unit, DataError.Network>
}