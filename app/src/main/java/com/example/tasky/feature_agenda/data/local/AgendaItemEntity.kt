package com.example.tasky.feature_agenda.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tasky.feature_agenda.data.model.EventDto
import com.example.tasky.feature_agenda.data.model.ReminderDto
import com.example.tasky.feature_agenda.data.model.TaskDto


data class AgendaItemsInfo(
    val events: List<EventDto> = emptyList(),
    val tasks: List<TaskDto> = emptyList(),
    val reminders: List<ReminderDto> = emptyList()
)

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean
)

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)