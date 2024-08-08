package com.example.tasky.feature_agenda.domain.local

import com.example.tasky.feature_agenda.data.local.EventEntity

interface EventLocalRepository {
    suspend fun insertEvent(event: EventEntity)
    suspend fun deleteEvent(event: EventEntity)
    suspend fun deleteAllEvents()
    suspend fun updateEvent(event: EventEntity)

}