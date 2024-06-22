package com.example.tasky.feature_login.domain.repository

import com.example.tasky.common.data.util.DataError
import com.example.tasky.common.data.util.Result
import com.example.tasky.feature_login.data.model.LoginUserInfo
import com.example.tasky.feature_login.data.model.LoginUserResponse
import com.example.tasky.feature_login.data.model.RegisterUserInfo

interface UserRemoteRepository {
    suspend fun registerUser(registerUserInfo: RegisterUserInfo): Result<Unit, DataError.Network>
    suspend fun logInUser(loginUserInfo: LoginUserInfo): Result<LoginUserResponse, DataError.Network>
}