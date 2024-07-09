package com.example.tasky.common.data.repository

import com.example.tasky.common.data.remote.TaskyApi
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.model.AccessTokenResponse
import com.example.tasky.common.domain.repository.TokenRemoteRepository
import com.example.tasky.common.domain.util.toNetworkErrorType
import com.example.tasky.di.TokenApi
import com.example.tasky.feature_login.data.model.AccessToken
import java.io.IOException


class TokenRemoteRepositoryImpl(
    @TokenApi private val api: TaskyApi
) : TokenRemoteRepository {
    override suspend fun refreshSession(
        refreshToken: String,
        userId: String
    ): Result<AccessTokenResponse, DataError.Network> {
        return try {
            val response =
                api.refreshSession(
                    accessToken = AccessToken(
                        refreshToken = refreshToken,
                        userId = userId
                    )
                )
            if (response.isSuccessful) {
                Result.Success(
                    AccessTokenResponse(
                        response.body()!!.accessToken,
                        response.body()!!.expirationTimestamp
                    )
                )
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun checkAuthentication(): Result<Unit, DataError.Network> {
        return try {
            val response = api.checkAuthentication()
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