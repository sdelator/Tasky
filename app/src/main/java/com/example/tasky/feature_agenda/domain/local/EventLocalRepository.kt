package com.example.tasky.feature_agenda.domain.local

import com.example.tasky.feature_agenda.data.local.EventEntity

interface EventLocalRepository {
    suspend fun insertEvent(eventEntity: EventEntity)
    suspend fun deleteEvent(eventEntity: EventEntity)
    suspend fun deleteAllEvents()
    suspend fun updateEvent(eventEntity: EventEntity)

}