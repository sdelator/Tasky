package com.example.tasky.feature_agenda.data.model

import com.example.tasky.agenda_details.domain.model.AgendaItem

fun AgendaItem.Task.toTaskDto(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt,
        isDone = this.isDone
    )
}