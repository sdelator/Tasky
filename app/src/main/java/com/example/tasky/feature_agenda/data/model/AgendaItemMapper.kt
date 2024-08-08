package com.example.tasky.feature_agenda.data.model

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.agenda_details.domain.model.AttendeeAccountDetails
import com.example.tasky.agenda_details.domain.model.AttendeeBasicInfoDetails
import com.example.tasky.agenda_details.domain.model.AttendeeDetails
import com.example.tasky.common.domain.util.convertMillisToZonedDateTime
import com.example.tasky.common.domain.util.toEpochMillis
import com.example.tasky.feature_agenda.data.local.EventEntity
import com.example.tasky.feature_agenda.data.local.ReminderEntity
import com.example.tasky.feature_agenda.data.local.TaskEntity

fun AgendaItem.Task.toTaskDto(): TaskDto {
    return TaskDto(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toEpochMillis(),
        remindAt = this.remindAt.toEpochMillis(),
        isDone = this.isDone
    )
}

fun AgendaItem.Reminder.toReminderDto(): ReminderDto {
    return ReminderDto(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toEpochMillis(),
        remindAt = this.remindAt.toEpochMillis()
    )
}

fun EventDto.toAgendaItemEvent(): AgendaItem.Event {
    return AgendaItem.Event(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from.convertMillisToZonedDateTime(),
        to = this.to.convertMillisToZonedDateTime(),
        remindAt = this.remindAt.convertMillisToZonedDateTime(),
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
        time = this.time.convertMillisToZonedDateTime(),
        remindAt = this.remindAt.convertMillisToZonedDateTime()
    )
}

fun TaskDto.toAgendaItemTask(): AgendaItem.Task {
    return AgendaItem.Task(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.convertMillisToZonedDateTime(),
        remindAt = this.remindAt.convertMillisToZonedDateTime(),
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

fun AgendaItem.Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toEpochMillis(),
        remindAt = this.remindAt.toEpochMillis(),
        isDone = this.isDone
    )
}

fun AgendaItem.Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        time = this.time.toEpochMillis(),
        remindAt = this.remindAt.toEpochMillis()
    )
}

fun AgendaItem.Event.toEventEntity(): EventEntity {
    return EventEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        from = this.from.toEpochMillis(),
        to = this.to.toEpochMillis(),
        remindAt = this.remindAt.toEpochMillis(),
        host = this.host,
        isUserEventCreator = this.isUserEventCreator,
//            attendees = this.attendees,
//            photos = this.photos
    )
}