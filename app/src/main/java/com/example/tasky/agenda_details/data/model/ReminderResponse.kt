package com.example.tasky.agenda_details.data.model

data class ReminderResponse(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)
