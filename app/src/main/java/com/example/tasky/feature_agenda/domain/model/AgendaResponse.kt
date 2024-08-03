package com.example.tasky.feature_agenda.domain.model

import com.example.tasky.agenda_details.data.model.EventResponse
import com.example.tasky.agenda_details.data.model.ReminderResponse
import com.example.tasky.agenda_details.data.model.TaskResponse

data class AgendaResponse(
    val events: List<EventResponse> = emptyList(),
    val tasks: List<TaskResponse> = emptyList(),
    val reminders: List<ReminderResponse> = emptyList()
)