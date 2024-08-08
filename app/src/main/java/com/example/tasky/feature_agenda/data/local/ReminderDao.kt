package com.example.tasky.feature_agenda.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders")
    fun getAllReminders(): List<ReminderEntity>

    @Upsert
    fun upsert(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = (:id)")
    fun delete(id: String)

    @Query("DELETE FROM reminders")
    fun deleteAllReminders()
}