package com.example.tasky.agenda_details.data.repository

import android.util.Log
import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.agenda_details.domain.model.EventDetails
import com.example.tasky.agenda_details.domain.repository.AgendaDetailsRemoteRepository
import com.example.tasky.common.data.remote.TaskyApi
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.util.toNetworkErrorType
import com.example.tasky.di.AuthenticatedApi
import com.example.tasky.feature_agenda.data.model.toReminderDto
import com.example.tasky.feature_agenda.data.model.toTaskDto
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

class AgendaDetailsRemoteRepositoryImpl(
    @AuthenticatedApi private val api: TaskyApi
) : AgendaDetailsRemoteRepository {
    override suspend fun createEvent(
        eventDetails: EventDetails,
        photosByteArray: List<ByteArray>
    ): Result<AgendaItem.Event, DataError.Network> {
        return try {
            val gson = Gson()
            val json = gson.toJson(eventDetails)
            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())

            val photoParts = photosByteArray.mapIndexed { index, byteArray ->
                val mimeType = when (byteArray.take(3)) {
                    listOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte()) -> "image/jpeg"
                    listOf(0x89.toByte(), 0x50.toByte(), 0x4e.toByte()) -> "image/png"
                    else -> throw Exception("invalid Image format")
                }
                val requestFile =
                    byteArray.toRequestBody(mimeType.toMediaTypeOrNull(), 0, byteArray.size)
                MultipartBody.Part.createFormData(
                    "photo$index",
                    "image$index.${mimeType.split("/")[1]}",
                    requestFile
                )
            }

            val response = api.createEvent(requestBody, photoParts)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.Success(
                        AgendaItem.Event(
                            id = responseBody.id,
                            title = responseBody.title,
                            description = responseBody.description,
                            from = responseBody.from,
                            to = responseBody.to,
                            remindAt = responseBody.remindAt,
                            host = responseBody.host,
                            isUserEventCreator = responseBody.isUserEventCreator,
                            attendees = responseBody.attendees,
                            photos = responseBody.photos
                        )
                    )
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

    override suspend fun getEvent(): Result<AgendaItem.Event, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(eventId: String): Result<Unit, DataError.Network> {
        return try {
            val response = api.deleteEvent(eventId = eventId)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.Success(Unit)
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

    override suspend fun updateEvent(): Result<AgendaItem.Event, DataError.Network> {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(
        taskDetails: AgendaItem.Task
    ): Result<Unit, DataError.Network> {
        return try {
            val task = taskDetails.toTaskDto()
            val response = api.createTask(task)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.Success(Unit)
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

    override suspend fun deleteTask(taskId: String): Result<Unit, DataError.Network> {
        return try {
            val response = api.deleteTask(taskId = taskId)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.Success(Unit)
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

    override suspend fun createReminder(
        reminderDetails: AgendaItem.Reminder
    ): Result<Unit, DataError.Network> {
        return try {
            val reminder = reminderDetails.toReminderDto()
            val response = api.createReminder(reminder)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.Success(Unit)
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

    override suspend fun deleteReminder(reminderId: String): Result<Unit, DataError.Network> {
        return try {
            val response = api.deleteReminder(reminderId = reminderId)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.Success(Unit)
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
}