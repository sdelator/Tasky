package com.example.tasky.feature_agenda.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [EventEntity::class, TaskEntity::class, ReminderEntity::class],
    version = 1
)
abstract class TaskyDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao

}