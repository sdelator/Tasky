package com.example.tasky.agenda_details.domain.repository

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.agenda_details.domain.model.EventDetails
import com.example.tasky.agenda_details.domain.model.Reminder
import com.example.tasky.agenda_details.domain.model.Task
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError

interface AgendaDetailsRemoteRepository {
    suspend fun createEvent(
        eventDetails: EventDetails,
        photosByteArray: List<ByteArray>
    ): Result<AgendaItem.Event, DataError.Network>

    suspend fun getEvent(): Result<AgendaItem.Event, DataError.Network>

    suspend fun deleteEvent(): Result<Unit, DataError.Network>

    suspend fun updateEvent(): Result<AgendaItem.Event, DataError.Network>

    suspend fun createTask(
        taskDetails: Task
    ): Result<Unit, DataError.Network>

    suspend fun createReminder(
        reminderDetails: Reminder
    ): Result<Unit, DataError.Network>
}