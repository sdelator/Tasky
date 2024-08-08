package com.example.tasky.feature_agenda.domain.local

import com.example.tasky.agenda_details.domain.model.AgendaItem

interface ReminderLocalRepository {
    suspend fun insertReminder(reminder: AgendaItem.Reminder)
    suspend fun deleteReminder(reminderId: String)
    suspend fun updateReminder(reminder: AgendaItem.Reminder)
    suspend fun deleteAllReminders()
}