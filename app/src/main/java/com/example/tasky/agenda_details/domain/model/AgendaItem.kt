package com.example.tasky.agenda_details.domain.model

import com.example.tasky.common.domain.model.AgendaItemType
import java.time.ZonedDateTime

sealed class AgendaItem(
    open val id: String,
    open val title: String,
    open val description: String?,
    open val startTime: ZonedDateTime,
    open val remindAt: ZonedDateTime,
    open val cardType: AgendaItemType
) {
    data class Event(
        override val id: String,
        override val title: String,
        override val description: String,
        val from: ZonedDateTime,
        val to: ZonedDateTime,
        override val remindAt: ZonedDateTime,
        val host: String,
        val isUserEventCreator: Boolean,
        val attendees: List<AttendeeDetails>,
        val photos: List<Photo>
    ) : AgendaItem(id, title, description, startTime = from, remindAt, AgendaItemType.Event)

    data class Task(
        override val id: String,
        override val title: String,
        override val description: String?,
        val time: ZonedDateTime,
        override val remindAt: ZonedDateTime,
        val isDone: Boolean
    ) : AgendaItem(id, title, description, startTime = time, remindAt, AgendaItemType.Task)

    data class Reminder(
        override val id: String,
        override val title: String,
        override val description: String?,
        val time: ZonedDateTime,
        override val remindAt: ZonedDateTime
    ) : AgendaItem(id, title, description, startTime = time, remindAt, AgendaItemType.Reminder)
}
