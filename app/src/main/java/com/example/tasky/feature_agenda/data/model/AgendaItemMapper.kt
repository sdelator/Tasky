package com.example.tasky.feature_agenda.data.model

import com.example.tasky.agenda_details.domain.model.AgendaItem

fun AgendaItem.Task.toTaskDto(): TaskDto {
    return TaskDto(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt,
        isDone = this.isDone
    )
}

fun AgendaItem.Reminder.toReminderDto(): ReminderDto {
    return ReminderDto(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt
    )
}

fun EventDto.toAgendaItemEvent(): AgendaItem.Event {
    return AgendaItem.Event(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from,
        to = this.to,
        remindAt = this.remindAt,
        host = this.host,
        isUserEventCreator = this.isUserEventCreator,
        attendees = this.attendees,
        photos = this.photos
    )
}

fun ReminderDto.toAgendaItemReminder(): AgendaItem.Reminder {
    return AgendaItem.Reminder(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt
    )
}

fun TaskDto.toAgendaItemTask(): AgendaItem.Task {
    return AgendaItem.Task(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time,
        remindAt = this.remindAt,
        isDone = this.isDone
    )
}