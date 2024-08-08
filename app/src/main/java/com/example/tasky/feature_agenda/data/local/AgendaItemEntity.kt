package com.example.tasky.feature_agenda.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tasky.agenda_details.domain.model.Photo
import com.example.tasky.feature_agenda.data.model.AttendeeDto
import com.example.tasky.feature_agenda.data.model.EventDto
import com.example.tasky.feature_agenda.data.model.ReminderDto
import com.example.tasky.feature_agenda.data.model.TaskDto


data class AgendaItemsInfo(
    val events: List<EventDto> = emptyList(),
    val tasks: List<TaskDto> = emptyList(),
    val reminders: List<ReminderDto> = emptyList()
)

@Entity(tableName = "events")
data class EventInfo(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<AttendeeDto>, // This will need a type converter
    val photos: List<Photo.RemotePhoto> // This will need a type converter
)

@Entity(tableName = "tasks")
data class TaskInfo(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean
)

@Entity(tableName = "reminders")
data class ReminderInfo(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)