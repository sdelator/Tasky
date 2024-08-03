package com.example.tasky.agenda_details.data.model

data class Task(
    val id: String,
    val title: String,
    val description: String?,
    val time: Long,
    val remindAt: Long,
    val isDone: Boolean
)
