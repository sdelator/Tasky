package com.example.tasky.feature_agenda.domain.local

interface EventLocalRepository {
    suspend fun insertEvent()
    suspend fun deleteEvent()
    suspend fun updateEvent()

}