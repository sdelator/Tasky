package com.example.tasky.common.domain.repository

import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.model.AccessTokenResponse

interface TokenRemoteRepository {
    suspend fun refreshSession(
        refreshToken: String,
        userId: String
    ): Result<AccessTokenResponse, DataError.Network>

    suspend fun checkAuthentication(): Result<Unit, DataError.Network>
}