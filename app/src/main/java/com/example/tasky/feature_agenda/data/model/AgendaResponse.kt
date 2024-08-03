package com.example.tasky.feature_agenda.data.model

data class AgendaResponse(
    val events: List<EventDto> = emptyList(),
    val tasks: List<TaskDto> = emptyList(),
    val reminders: List<ReminderDto> = emptyList()
)