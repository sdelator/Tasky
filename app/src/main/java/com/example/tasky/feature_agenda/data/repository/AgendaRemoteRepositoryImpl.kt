package com.example.tasky.feature_agenda.data.repository

import android.util.Log
import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.common.data.remote.TaskyApi
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.util.toNetworkErrorType
import com.example.tasky.di.AuthenticatedApi
import com.example.tasky.feature_agenda.domain.repository.AgendaRemoteRepository
import java.io.IOException

class AgendaRemoteRepositoryImpl(
    @AuthenticatedApi private val api: TaskyApi
) : AgendaRemoteRepository {
    override suspend fun loadAgenda(time: Long): Result<List<AgendaItem>, DataError.Network> {
        return try {
            val response = api.loadAgenda(time)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val events = responseBody.events.map { event ->
                        AgendaItem.Event(
                            id = event.id,
                            title = event.title,
                            description = event.description ?: "",
                            from = event.from,
                            to = event.to,
                            remindAt = event.remindAt,
                            host = event.host,
                            isUserEventCreator = event.isUserEventCreator,
                            attendees = event.attendees,
                            photos = event.photos
                        )
                    }
                    val tasks = responseBody.tasks.map { task ->
                        AgendaItem.Task(
                            id = task.id,
                            title = task.title,
                            description = task.description,
                            time = task.time,
                            remindAt = task.remindAt,
                            isDone = task.isDone
                        )
                    }
                    val reminders = responseBody.reminders.map { reminder ->
                        AgendaItem.Reminder(
                            id = reminder.id,
                            title = reminder.title,
                            description = reminder.description,
                            time = reminder.time,
                            remindAt = reminder.remindAt
                        )
                    }
                    val agendaItemList = events + tasks + reminders
                    val agendaSortedByTime =
                        agendaItemList.sortedBy { agendaItem -> agendaItem.startTime }

                    Result.Success(agendaSortedByTime)
                } else {
                    Log.e("Error", "API call failed with code ${response.code()}")
                    Result.Error(DataError.Network.SERVER_ERROR)
                }
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
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val events = responseBody.events.map { event ->
                        AgendaItem.Event(
                            id = event.id,
                            title = event.title,
                            description = event.description ?: "",
                            from = event.from,
                            to = event.to,
                            remindAt = event.remindAt,
                            host = event.host,
                            isUserEventCreator = event.isUserEventCreator,
                            attendees = event.attendees,
                            photos = event.photos
                        )
                    }
                    val tasks = responseBody.tasks.map { task ->
                        AgendaItem.Task(
                            id = task.id,
                            title = task.title,
                            description = task.description,
                            time = task.time,
                            remindAt = task.remindAt,
                            isDone = task.isDone
                        )
                    }
                    val reminders = responseBody.reminders.map { reminder ->
                        AgendaItem.Reminder(
                            id = reminder.id,
                            title = reminder.title,
                            description = reminder.description,
                            time = reminder.time,
                            remindAt = reminder.remindAt
                        )
                    }
                    val agendaItemList = events + tasks + reminders
                    //todo do we want to sort here by time ?
                    Result.Success(agendaItemList)
                } else {
                    Result.Error(DataError.Network.SERVER_ERROR)
                }
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