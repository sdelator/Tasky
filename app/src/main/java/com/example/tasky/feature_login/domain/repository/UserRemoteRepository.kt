package com.example.tasky.feature_login.domain.repository

import com.example.tasky.common.domain.util.DataError
import com.example.tasky.common.domain.util.Result
import com.example.tasky.feature_login.domain.model.LoginUserInfo
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import com.example.tasky.feature_login.domain.model.RegisterUserInfo

interface UserRemoteRepository {
    suspend fun postRegisterCall(registerUserInfo: RegisterUserInfo): Result<Unit, DataError.Network>
    suspend fun postLoginCall(loginUserInfo: LoginUserInfo): Result<LoginUserResponse, DataError.Network>
}