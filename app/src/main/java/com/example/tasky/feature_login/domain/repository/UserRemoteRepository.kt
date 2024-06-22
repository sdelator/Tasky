package com.example.tasky.feature_login.domain.repository

import com.example.tasky.common.data.util.DataError
import com.example.tasky.common.data.util.Result
import com.example.tasky.feature_login.data.model.LoginUserResponse

interface UserRemoteRepository {
    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Result<Unit, DataError.Network>

    suspend fun logInUser(
        email: String,
        password: String
    ): Result<LoginUserResponse, DataError.Network>
}