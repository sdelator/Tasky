package com.example.tasky.agenda_details.domain.repository

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.agenda_details.domain.model.EventDetails
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError

interface AgendaDetailsRemoteRepository {
    suspend fun createEvent(
        eventDetails: EventDetails,
        photosByteArray: List<ByteArray>
    ): Result<AgendaItem.Event, DataError.Network>

    suspend fun loadEvent(eventId: String): Result<AgendaItem.Event, DataError.Network>

    suspend fun deleteEvent(eventId: String): Result<Unit, DataError.Network>

    suspend fun updateEvent(): Result<AgendaItem.Event, DataError.Network>

    suspend fun createTask(
        taskDetails: AgendaItem.Task
    ): Result<Unit, DataError.Network>

    suspend fun loadTask(taskId: String): Result<AgendaItem.Task, DataError.Network>

    suspend fun deleteTask(taskId: String): Result<Unit, DataError.Network>

    suspend fun createReminder(
        reminderDetails: AgendaItem.Reminder
    ): Result<Unit, DataError.Network>

    suspend fun loadReminder(reminderId: String): Result<AgendaItem.Reminder, DataError.Network>

    suspend fun deleteReminder(reminderId: String): Result<Unit, DataError.Network>
}