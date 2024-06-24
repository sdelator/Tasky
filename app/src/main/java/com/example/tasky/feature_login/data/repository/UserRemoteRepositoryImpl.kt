package com.example.tasky.feature_login.data.repository

import android.app.Application
import com.example.tasky.R
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.util.toNetworkErrorType
import com.example.tasky.feature_login.data.model.LoginUserInfo
import com.example.tasky.feature_login.data.model.RegisterUserInfo
import com.example.tasky.feature_login.data.remote.TaskyApi
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import okio.IOException

class UserRemoteRemoteRepositoryImpl(
    private val api: TaskyApi,
    private val appContext: Application
) : UserRemoteRepository {

    init {
        val appName = appContext.getString(R.string.app_name)
        println("hello from the repository. The appname is $appName")
    }

    override suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Result<Unit, DataError.Network> {
        return try {
            val response =
                api.registerUser(registerUserInfo = RegisterUserInfo(name, email, password))
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun logInUser(
        email: String,
        password: String
    ): Result<LoginUserResponse, DataError.Network> {
        return try {
            val response = api.loginUser(loginUserInfo = LoginUserInfo(email, password))
            val loginUserResponse = response.body()
            if (response.isSuccessful && loginUserResponse != null) {
                Result.Success(loginUserResponse)
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun logOutUser(): Result<Unit, DataError.Network> {
        return try {
            val response = api.logout()
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }
}