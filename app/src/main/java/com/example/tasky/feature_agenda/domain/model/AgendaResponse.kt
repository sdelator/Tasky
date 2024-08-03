package com.example.tasky.feature_agenda.domain.model

import com.example.tasky.agenda_details.data.model.Event
import com.example.tasky.agenda_details.data.model.Reminder
import com.example.tasky.agenda_details.data.model.Task

data class AgendaResponse(
    val events: List<Event> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val reminders: List<Reminder> = emptyList()
)