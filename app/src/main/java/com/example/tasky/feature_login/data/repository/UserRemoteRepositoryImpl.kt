package com.example.tasky.feature_login.data.repository

import android.app.Application
import com.example.tasky.R
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.util.APIUtilFunctions
import com.example.tasky.feature_login.data.model.LoginUserInfo
import com.example.tasky.feature_login.data.model.RegisterUserInfo
import com.example.tasky.feature_login.data.remote.TaskyApi
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import okio.IOException

class UserRemoteRemoteRepositoryImpl(
    private val api: TaskyApi,
    private val appContext: Application,
    private val apiUtilFunctions: APIUtilFunctions
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
                val error = when (response.code()) {
                    401 -> DataError.Network.UNAUTHORIZED
                    408 -> DataError.Network.REQUEST_TIMEOUT
                    413 -> DataError.Network.PAYLOAD_TOO_LARGE
                    else -> DataError.Network.UNKNOWN
                }
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
                val error = when (response.code()) {
                    401 -> DataError.Network.UNAUTHORIZED
                    408 -> DataError.Network.REQUEST_TIMEOUT
                    413 -> DataError.Network.PAYLOAD_TOO_LARGE
                    else -> DataError.Network.UNKNOWN
                }
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }
}