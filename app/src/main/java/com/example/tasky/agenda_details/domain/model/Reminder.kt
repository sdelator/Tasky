package com.example.tasky.agenda_details.domain.model

data class Reminder(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long
)
