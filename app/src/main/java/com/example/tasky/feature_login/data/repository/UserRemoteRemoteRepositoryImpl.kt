package com.example.tasky.feature_login.data.repository

import android.app.Application
import com.example.tasky.R
import com.example.tasky.feature_login.data.remote.TaskyApi
import com.example.tasky.feature_login.domain.model.RegisterUserInfo
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository

class UserRemoteRemoteRepositoryImpl(
    private val api: TaskyApi,
    private val appContext: Application
) : UserRemoteRepository {

    init {
        val appName = appContext.getString(R.string.app_name)
        println("hello from the repository. The appname is $appName")
    }

    override suspend fun postLoginCall(registerUserInfo: RegisterUserInfo) {
        api.registerUser(registerUserInfo = registerUserInfo)
    }
}