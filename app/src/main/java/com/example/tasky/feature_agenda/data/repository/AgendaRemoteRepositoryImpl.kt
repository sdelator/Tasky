package com.example.tasky.feature_agenda.data.repository

import com.example.tasky.common.data.remote.TaskyApi
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.util.toNetworkErrorType
import com.example.tasky.di.AuthenticatedApi
import com.example.tasky.feature_agenda.domain.model.AgendaResponse
import com.example.tasky.feature_agenda.domain.model.SyncAgendaResponse
import com.example.tasky.feature_agenda.domain.repository.AgendaRemoteRepository
import java.io.IOException

class AgendaRemoteRepositoryImpl(
    @AuthenticatedApi private val api: TaskyApi
) : AgendaRemoteRepository {
    override suspend fun loadAgenda(time: Long): Result<AgendaResponse, DataError.Network> {
        return try {
            val response = api.loadAgenda(time)
            if (response.isSuccessful) {
                val responseBody = response.body()
                Result.Success(
                    AgendaResponse(
                        events = responseBody?.events ?: emptyList(),
                        tasks = responseBody?.tasks ?: emptyList(),
                        reminders = responseBody?.reminders ?: emptyList()
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

    override suspend fun loadFullAgenda(): Result<AgendaResponse, DataError.Network> {
        return try {
            val response = api.loadFullAgenda()
            if (response.isSuccessful) {
                val responseBody = response.body()
                Result.Success(
                    AgendaResponse(
                        events = responseBody?.events ?: emptyList(),
                        tasks = responseBody?.tasks ?: emptyList(),
                        reminders = responseBody?.reminders ?: emptyList()
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

    override suspend fun syncAgenda(): Result<SyncAgendaResponse, DataError.Network> {
        TODO("Not yet implemented")
    }
}