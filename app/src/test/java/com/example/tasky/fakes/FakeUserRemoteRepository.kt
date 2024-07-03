package com.example.tasky.fakes

import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository

class FakeUserRemoteRepository : UserRemoteRepository {
    var shouldReturnSuccess = false
    override suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Result<Unit, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun logInUser(
        email: String,
        password: String
    ): Result<LoginUserResponse, DataError.Network> {
        TODO("Not yet implemented")
    }
}