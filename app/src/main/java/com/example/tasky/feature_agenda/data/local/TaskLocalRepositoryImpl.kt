package com.example.tasky.feature_agenda.data.local

import com.example.tasky.feature_agenda.domain.local.TaskLocalRepository
import javax.inject.Inject

class TaskLocalRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskLocalRepository {
    override suspend fun insertTask(task: TaskEntity) {
        taskDao.upsert(task)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }

    override suspend fun updateTask(task: TaskEntity) {
        taskDao.upsert(task)
    }

    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}