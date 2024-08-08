package com.example.tasky.feature_agenda.domain.local

interface ReminderLocalRepository {
    suspend fun insertReminder()
    suspend fun deleteReminder()
    suspend fun updateReminder()

}