package com.example.tasky.feature_agenda.domain.local

import com.example.tasky.agenda_details.domain.model.AgendaItem

interface EventLocalRepository {
    suspend fun insertEvent(event: AgendaItem.Event)
    suspend fun deleteEvent(eventId: String)
    suspend fun deleteAllEvents()
    suspend fun updateEvent(event: AgendaItem.Event)

}