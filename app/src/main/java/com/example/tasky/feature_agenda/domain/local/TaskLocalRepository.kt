package com.example.tasky.feature_agenda.domain.local

interface TaskLocalRepository {
    suspend fun insertTask()
    suspend fun deleteTask()
    suspend fun updateTask()

}