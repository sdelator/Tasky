package com.example.tasky.feature_login.data.repository

import android.app.Application
import com.example.tasky.R
import com.example.tasky.common.data.util.APIUtilFunctions
import com.example.tasky.common.data.util.DataError
import com.example.tasky.common.data.util.Result
import com.example.tasky.feature_login.data.remote.TaskyApi
import com.example.tasky.feature_login.domain.model.LoginUserInfo
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import com.example.tasky.feature_login.domain.model.RegisterUserInfo
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import okio.IOException
import retrofit2.HttpException

class UserRemoteRemoteRepositoryImpl(
    private val api: TaskyApi,
    private val appContext: Application,
    private val apiUtilFunctions: APIUtilFunctions
) : UserRemoteRepository {

    init {
        val appName = appContext.getString(R.string.app_name)
        println("hello from the repository. The appname is $appName")
    }

    override suspend fun postRegisterCall(registerUserInfo: RegisterUserInfo): Result<Unit, DataError.Network> {
        return try {
            val response = api.registerUser(registerUserInfo = registerUserInfo)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                val errorMessage =
                    apiUtilFunctions.parseErrorResponse(response.errorBody()?.string())
                val error = when (response.code()) {
                    401 -> DataError.Network.Unauthorized(errorMessage)
                    408 -> DataError.Network.RequestTimeout(errorMessage)
                    413 -> DataError.Network.PayloadTooLarge(errorMessage)
                    else -> DataError.Network.Unknown(errorMessage)
                }

                Result.Error(error)
            }
        } catch (e: HttpException) {
            val error = apiUtilFunctions.getHttpErrorMessage(e)
            Result.Error(error)
        } catch (e: IOException) {
            Result.Error(DataError.Network.NoInternet(e.message ?: ""))
        }
    }

    override suspend fun postLoginCall(loginUserInfo: LoginUserInfo): Result<LoginUserResponse, DataError.Network> {
        return try {
            val response = api.loginUser(loginUserInfo = loginUserInfo)
            val loginUserResponse = response.body()
            if (response.isSuccessful && loginUserResponse != null) {
                Result.Success(loginUserResponse)
            } else {
                val errorMessage =
                    apiUtilFunctions.parseErrorResponse(response.errorBody()?.string())
                val error = when (response.code()) {
                    401 -> DataError.Network.Unauthorized(errorMessage)
                    408 -> DataError.Network.RequestTimeout(errorMessage)
                    413 -> DataError.Network.PayloadTooLarge(errorMessage)
                    else -> DataError.Network.Unknown(errorMessage)
                }

                Result.Error(error)
            }
        } catch (e: HttpException) {
            val error = apiUtilFunctions.getHttpErrorMessage(e)
            Result.Error(error)
        } catch (e: IOException) {
            Result.Error(DataError.Network.NoInternet(e.message ?: ""))
        }
    }
}