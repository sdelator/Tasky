package com.example.tasky.feature_agenda.data.local

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.feature_agenda.data.model.toEventEntity
import com.example.tasky.feature_agenda.domain.local.EventLocalRepository
import javax.inject.Inject

class EventLocalRepositoryImpl @Inject constructor(
    private val eventDao: EventDao
) : EventLocalRepository {
    override suspend fun insertEvent(event: AgendaItem.Event) {
        eventDao.upsert(event.toEventEntity())
    }

    override suspend fun deleteEvent(eventId: String) {
        eventDao.delete(eventId)
    }

    override suspend fun deleteAllEvents() {
        eventDao.deleteAllEvents()
    }

    override suspend fun updateEvent(event: AgendaItem.Event) {
        eventDao.upsert(event = event.toEventEntity())
    }
}