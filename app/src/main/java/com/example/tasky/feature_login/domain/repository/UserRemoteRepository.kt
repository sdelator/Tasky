package com.example.tasky.feature_login.domain.repository

import com.example.tasky.feature_login.domain.model.RegisterUserInfo

interface UserRemoteRepository {
    suspend fun postLoginCall(registerUserInfo: RegisterUserInfo)
}