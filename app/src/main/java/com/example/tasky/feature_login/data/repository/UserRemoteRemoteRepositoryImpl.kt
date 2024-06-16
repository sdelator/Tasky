package com.example.tasky.feature_login.data.repository

import android.app.Application
import com.example.tasky.R
import com.example.tasky.common.model.Resource
import com.example.tasky.common.model.ResponseHandler
import com.example.tasky.feature_login.data.remote.TaskyApi
import com.example.tasky.feature_login.domain.model.LoginUserInfo
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import com.example.tasky.feature_login.domain.model.RegisterUserInfo
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import org.json.JSONObject
import retrofit2.HttpException

class UserRemoteRemoteRepositoryImpl(
    private val api: TaskyApi,
    private val responseHandler: ResponseHandler,
    private val appContext: Application
) : UserRemoteRepository {

    init {
        val appName = appContext.getString(R.string.app_name)
        println("hello from the repository. The appname is $appName")
    }

    override suspend fun postRegisterCall(registerUserInfo: RegisterUserInfo): Resource<Unit> {
        return try {
            val response = api.registerUser(registerUserInfo = registerUserInfo)
            if (response.isSuccessful) {
                responseHandler.handleSuccess(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage =
                    errorBody?.let { JSONObject(it).getString("message") } ?: "Unknown error"
                responseHandler.handleFailure(HttpException(response), errorMessage)
            }
        } catch (e: Exception) {
            responseHandler.handleFailure(e, e.message ?: "Unknown error")
        }
    }

    override suspend fun postLoginCall(loginUserInfo: LoginUserInfo): Resource<LoginUserResponse> {
        return try {
            responseHandler.handleSuccess(api.loginUser(loginUserInfo = loginUserInfo))
        } catch (e: Exception) {
            responseHandler.handleFailure(e)
        }
    }

}