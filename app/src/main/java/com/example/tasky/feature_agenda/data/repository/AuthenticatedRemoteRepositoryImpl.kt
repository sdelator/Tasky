package com.example.tasky.feature_agenda.data.repository

import com.example.tasky.common.data.remote.TaskyApi
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.util.toNetworkErrorType
import com.example.tasky.di.AuthenticatedApi
import com.example.tasky.feature_agenda.domain.repository.AuthenticatedRemoteRepository
import java.io.IOException


class AuthenticatedRemoteRepositoryImpl(
    @AuthenticatedApi private val api: TaskyApi
) : AuthenticatedRemoteRepository {
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

    override suspend fun authenticate(): Result<Unit, DataError.Network> {
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