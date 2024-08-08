package com.example.tasky.feature_agenda.data.local

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.feature_agenda.data.model.toReminderEntity
import com.example.tasky.feature_agenda.domain.local.ReminderLocalRepository
import javax.inject.Inject

class ReminderLocalRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderLocalRepository {
    override suspend fun insertReminder(reminder: AgendaItem.Reminder) {
        reminderDao.upsert(reminder.toReminderEntity())
    }

    override suspend fun deleteReminder(reminderId: String) {
        reminderDao.delete(reminderId)
    }

    override suspend fun updateReminder(reminder: AgendaItem.Reminder) {
        reminderDao.upsert(reminder.toReminderEntity())
    }

    override suspend fun deleteAllReminders() {
        reminderDao.deleteAllReminders()
    }
}