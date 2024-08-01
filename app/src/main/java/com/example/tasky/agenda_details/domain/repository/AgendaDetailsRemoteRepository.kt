package com.example.tasky.agenda_details.domain.repository

import com.example.tasky.agenda_details.data.model.EventDetails
import com.example.tasky.agenda_details.domain.model.EventResponse
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.error.DataError

interface AgendaDetailsRemoteRepository {
    suspend fun createEvent(
        eventDetails: EventDetails,
        photosByteArray: List<ByteArray>
    ): Result<EventResponse, DataError.Network>

    suspend fun getEvent(): Result<EventResponse, DataError.Network>

    suspend fun deleteEvent(): Result<Unit, DataError.Network>

    suspend fun updateEvent(): Result<EventResponse, DataError.Network>
}