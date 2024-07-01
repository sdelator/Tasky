package com.example.tasky.feature_agenda.domain.repository

import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError

interface AuthenticatedRemoteRepository {
    suspend fun logOutUser(): Result<Unit, DataError.Network>
}