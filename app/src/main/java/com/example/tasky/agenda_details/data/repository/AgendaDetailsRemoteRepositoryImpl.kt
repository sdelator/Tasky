package com.example.tasky.agenda_details.data.repository

import android.content.ContentResolver
import com.example.tasky.agenda_details.data.MimeUtils
import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.agenda_details.domain.model.AttendeeAccountDetails
import com.example.tasky.agenda_details.domain.model.EventDetails
import com.example.tasky.agenda_details.domain.model.EventDetailsUpdated
import com.example.tasky.agenda_details.domain.model.Photo
import com.example.tasky.agenda_details.domain.repository.AgendaDetailsRemoteRepository
import com.example.tasky.common.data.remote.TaskyApi
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.util.toNetworkErrorType
import com.example.tasky.di.AuthenticatedApi
import com.example.tasky.feature_agenda.data.model.toAgendaItemEvent
import com.example.tasky.feature_agenda.data.model.toAgendaItemReminder
import com.example.tasky.feature_agenda.data.model.toAgendaItemTask
import com.example.tasky.feature_agenda.data.model.toAttendeeAccountDetails
import com.example.tasky.feature_agenda.data.model.toReminderDto
import com.example.tasky.feature_agenda.data.model.toTaskDto
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

class AgendaDetailsRemoteRepositoryImpl(
    @AuthenticatedApi private val api: TaskyApi,
    private val contentResolver: ContentResolver
) : AgendaDetailsRemoteRepository {
    override suspend fun createEvent(
        eventDetails: EventDetails,
        photos: List<Photo.LocalPhoto>
    ): Result<AgendaItem.Event, DataError.Network> {
        return try {
            val requestBody = createRequestBodyFromJson(eventDetails)
            val photoParts = createPhotoParts(photos)

            val response = api.createEvent(requestBody, photoParts)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                Result.Success(responseBody.toAgendaItemEvent())
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    private fun <T> createRequestBodyFromJson(data: T): RequestBody {
        val gson = Gson()
        val json = gson.toJson(data)
        return json.toRequestBody("application/json; charset=utf-8".toMediaType())
    }

    private fun createPhotoParts(photos: List<Photo.LocalPhoto>) =
        photos.mapIndexed { index, localPhoto ->
            val type = MimeUtils.getMimeType(contentResolver, localPhoto.uri)
            val extension = MimeUtils.getExtensionFromMimeType(type)
            val requestFile = MimeUtils.toRequestBody(localPhoto.byteArray, type)
            MultipartBody.Part.createFormData(
                "photo$index",
                "image$index.${extension}",
                requestFile
            )
        }

    override suspend fun loadEvent(eventId: String): Result<AgendaItem.Event, DataError.Network> {
        return try {
            val response = api.loadEvent(eventId = eventId)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                Result.Success(responseBody.toAgendaItemEvent())
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun deleteEvent(eventId: String): Result<Unit, DataError.Network> {
        return try {
            val response = api.deleteEvent(eventId = eventId)

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

    override suspend fun updateEvent(
        eventDetails: EventDetailsUpdated,
        photos: List<Photo.LocalPhoto>
    ): Result<AgendaItem.Event, DataError.Network> {
        return try {
            val requestBody = createRequestBodyFromJson(eventDetails)
            val photoParts = createPhotoParts(photos)

            val response = api.updateEvent(requestBody, photoParts)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                Result.Success(responseBody.toAgendaItemEvent())
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun getAttendee(attendeeEmail: String): Result<AttendeeAccountDetails, DataError.Network> {
        return try {
            val response = api.getAttendee(attendeeEmail)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                Result.Success(responseBody.toAttendeeAccountDetails())
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun deleteAttendee(eventId: String): Result<Unit, DataError.Network> {
        return try {
            val response = api.deleteEvent(eventId = eventId)

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

    override suspend fun createTask(
        taskDetails: AgendaItem.Task
    ): Result<Unit, DataError.Network> {
        return try {
            val task = taskDetails.toTaskDto()
            val response = api.createTask(task)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                Result.Success(Unit)
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun loadTask(taskId: String): Result<AgendaItem.Task, DataError.Network> {
        return try {
            val response = api.loadTask(taskId = taskId)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                Result.Success(responseBody.toAgendaItemTask())
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
                Result.Success(Unit)
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun updateTask(taskDetails: AgendaItem.Task): Result<Unit, DataError.Network> {
        return try {
            val task = taskDetails.toTaskDto()
            val response = api.updateTask(task)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                Result.Success(Unit)
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
                Result.Success(Unit)
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun loadReminder(
        reminderId: String
    ): Result<AgendaItem.Reminder, DataError.Network> {
        return try {
            val response = api.loadReminder(reminderId)
            val responseBody = response.body()

            if (response.isSuccessful && responseBody != null) {
                Result.Success(responseBody.toAgendaItemReminder())
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
                Result.Success(Unit)
            } else {
                val error = response.code().toNetworkErrorType()
                Result.Error(error)
            }
        } catch (e: IOException) {
            Result.Error(DataError.Network.NO_INTERNET)
        }
    }

    override suspend fun updateReminder(reminderDetails: AgendaItem.Reminder): Result<Unit, DataError.Network> {
        return try {
            val reminder = reminderDetails.toReminderDto()
            val response = api.updateReminder(reminder)

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