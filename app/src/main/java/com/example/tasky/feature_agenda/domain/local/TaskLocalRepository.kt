package com.example.tasky.feature_agenda.domain.local

import com.example.tasky.agenda_details.domain.model.AgendaItem

interface TaskLocalRepository {
    suspend fun insertTask(task: AgendaItem.Task)
    suspend fun deleteTask(taskId: String)
    suspend fun updateTask(task: AgendaItem.Task)
    suspend fun deleteAllTasks()
}