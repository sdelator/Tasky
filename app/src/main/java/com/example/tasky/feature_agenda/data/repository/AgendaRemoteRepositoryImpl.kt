package com.example.tasky.feature_agenda.data.repository

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.common.data.remote.TaskyApi
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.util.toNetworkErrorType
import com.example.tasky.di.AuthenticatedApi
import com.example.tasky.feature_agenda.data.model.toAgendaItemEvent
import com.example.tasky.feature_agenda.data.model.toAgendaItemReminder
import com.example.tasky.feature_agenda.data.model.toAgendaItemTask
import com.example.tasky.feature_agenda.domain.repository.AgendaRemoteRepository
import java.io.IOException

class AgendaRemoteRepositoryImpl(
    @AuthenticatedApi private val api: TaskyApi
) : AgendaRemoteRepository {
    override suspend fun loadAgenda(time: Long): Result<List<AgendaItem>, DataError.Network> {
        return try {
            val response = api.loadAgenda(time)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val events = responseBody.events.map { event ->
                    event.toAgendaItemEvent()
                }
                val tasks = responseBody.tasks.map { task ->
                    task.toAgendaItemTask()
                }
                val reminders = responseBody.reminders.map { reminder ->
                    reminder.toAgendaItemReminder()
                }
                val agendaItemList = events + tasks + reminders
                val agendaSortedByTime =
                    agendaItemList.sortedBy { agendaItem -> agendaItem.startTime }

                Result.Success(agendaSortedByTime)
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun loadFullAgenda(): Result<List<AgendaItem>, DataError.Network> {
        return try {
            val response = api.loadFullAgenda()
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val events = responseBody.events.map { event ->
                    event.toAgendaItemEvent()
                }
                val tasks = responseBody.tasks.map { task ->
                    task.toAgendaItemTask()
                }
                val reminders = responseBody.reminders.map { reminder ->
                    reminder.toAgendaItemReminder()
                }
                val agendaItemList = events + tasks + reminders
                val agendaSortedByTime =
                    agendaItemList.sortedBy { agendaItem -> agendaItem.startTime }

                Result.Success(agendaSortedByTime)
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

//    override suspend fun syncAgenda(): Result<SyncAgendaResponse, DataError.Network> {
//        TODO("Not yet implemented")
//    }
}