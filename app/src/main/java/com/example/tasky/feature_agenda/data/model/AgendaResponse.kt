package com.example.tasky.feature_agenda.data.model

data class AgendaResponse(
    val events: List<Event> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val reminders: List<Reminder> = emptyList()
)