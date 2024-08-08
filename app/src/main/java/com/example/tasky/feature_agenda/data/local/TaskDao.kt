package com.example.tasky.feature_agenda.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskInfo>

    @Upsert
    fun upsert(task: TaskInfo)

    @Delete
    fun delete(task: TaskInfo)
}
