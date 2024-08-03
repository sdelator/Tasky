package com.example.tasky.feature_agenda.data.model

import com.example.tasky.agenda_details.domain.model.Event
import com.example.tasky.agenda_details.domain.model.Reminder
import com.example.tasky.agenda_details.domain.model.Task

data class AgendaResponse(
    val events: List<Event> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val reminders: List<Reminder> = emptyList()
)