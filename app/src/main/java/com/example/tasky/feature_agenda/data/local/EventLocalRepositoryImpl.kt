package com.example.tasky.feature_agenda.data.local

import com.example.tasky.feature_agenda.domain.local.EventLocalRepository
import javax.inject.Inject

class EventLocalRepositoryImpl @Inject constructor(
    private val eventDao: EventDao
) : EventLocalRepository {
    override suspend fun insertEvent(eventEntity: EventEntity) {
        eventDao.upsert(eventEntity)
    }

    override suspend fun deleteEvent(eventEntity: EventEntity) {
        eventDao.delete(event = eventEntity)
    }

    override suspend fun deleteAllEvents() {
        eventDao.deleteAllEvents()
    }

    override suspend fun updateEvent(eventEntity: EventEntity) {
        eventDao.upsert(event = eventEntity)
    }
}