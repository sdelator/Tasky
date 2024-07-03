package com.example.tasky.fakes

import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.feature_agenda.domain.repository.AuthenticatedRemoteRepository

class FakeAuthenticatedRemoteRepository : AuthenticatedRemoteRepository {
    var shouldReturnSuccess = false
    override suspend fun logOutUser(): Result<Unit, DataError.Network> {
        return if (shouldReturnSuccess) {
            Result.Success(Unit)
        } else {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }
}