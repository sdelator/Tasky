package com.example.tasky.feature_login.domain.repository

import com.example.tasky.common.model.Resource
import com.example.tasky.feature_login.domain.model.LoginUserInfo
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import com.example.tasky.feature_login.domain.model.RegisterUserInfo

interface UserRemoteRepository {
    suspend fun postRegisterCall(registerUserInfo: RegisterUserInfo): Resource<Unit>
    suspend fun postLoginCall(loginUserInfo: LoginUserInfo): Resource<LoginUserResponse>
}