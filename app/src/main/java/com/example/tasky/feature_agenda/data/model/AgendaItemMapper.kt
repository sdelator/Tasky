package com.example.tasky.feature_agenda.data.model

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.agenda_details.domain.model.AttendeeAccountDetails
import com.example.tasky.agenda_details.domain.model.AttendeeBasicInfoDetails
import com.example.tasky.agenda_details.domain.model.AttendeeDetails

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
        attendees = this.attendees.map { attendee -> attendee.toAttendeeDetails() },
        photos = this.photos
    )
}

fun AttendeeDto.toAttendeeDetails(): AttendeeDetails {
    return AttendeeDetails(
        email = this.email,
        fullName = this.fullName,
        userId = this.userId,
        eventId = this.eventId,
        isGoing = this.isGoing,
        remindAt = this.remindAt
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

fun AttendeeAccountDto.toAttendeeAccountDetails(): AttendeeAccountDetails {
    return AttendeeAccountDetails(
        doesUserExist = this.doesUserExist,
        attendee = this.attendee?.toAttendeeBasicInfoDetails()
    )
}

fun AttendeeBasicInfoDto.toAttendeeBasicInfoDetails(): AttendeeBasicInfoDetails {
    return AttendeeBasicInfoDetails(
        email = this.email,
        fullName = this.fullName,
        userId = this.userId
    )
}