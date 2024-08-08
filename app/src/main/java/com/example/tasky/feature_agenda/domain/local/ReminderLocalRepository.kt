package com.example.tasky.feature_agenda.domain.local

import com.example.tasky.feature_agenda.data.local.ReminderEntity

interface ReminderLocalRepository {
    suspend fun insertReminder(reminder: ReminderEntity)
    suspend fun deleteReminder(reminderId: String)
    suspend fun updateReminder(reminder: ReminderEntity)
    suspend fun deleteAllReminders()
}