package com.example.tasky.feature_agenda.domain.local

import com.example.tasky.feature_agenda.data.local.TaskEntity

interface TaskLocalRepository {
    suspend fun insertTask(task: TaskEntity)
    suspend fun deleteTask(taskId: String)
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteAllTasks()
}