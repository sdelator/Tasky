package com.example.tasky.feature_agenda.data.local

import com.example.tasky.feature_agenda.domain.local.EventLocalRepository
import javax.inject.Inject

class EventLocalRepositoryImpl @Inject constructor(
    private val eventDao: EventDao
) : EventLocalRepository {
    override suspend fun insertEvent(event: EventEntity) {
        eventDao.upsert(event)
    }

    override suspend fun deleteEvent(eventId: String) {
        eventDao.delete(eventId)
    }

    override suspend fun deleteAllEvents() {
        eventDao.deleteAllEvents()
    }

    override suspend fun updateEvent(event: EventEntity) {
        eventDao.upsert(event = event)
    }
}