package com.example.tasky.feature_agenda.data.local

import com.example.tasky.feature_agenda.domain.local.ReminderLocalRepository
import javax.inject.Inject

class ReminderLocalRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderLocalRepository {
    override suspend fun insertReminder(reminder: ReminderEntity) {
        reminderDao.upsert(reminder)
    }

    override suspend fun deleteReminder(reminderId: String) {
        reminderDao.delete(reminderId)
    }

    override suspend fun updateReminder(reminder: ReminderEntity) {
        reminderDao.upsert(reminder)
    }

    override suspend fun deleteAllReminders() {
        reminderDao.deleteAllReminders()
    }
}