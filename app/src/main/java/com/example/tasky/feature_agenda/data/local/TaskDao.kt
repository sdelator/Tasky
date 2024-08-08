package com.example.tasky.feature_agenda.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<TaskEntity>

    @Upsert
    fun upsert(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = (:id)")
    fun delete(id: String)

    @Query("DELETE FROM tasks")
    fun deleteAllTasks()
}
