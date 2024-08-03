package com.example.tasky.agenda_details.domain.model

import com.example.tasky.common.presentation.model.AgendaItemType
import com.example.tasky.feature_agenda.data.model.Attendee

sealed class AgendaItem(
    open val id: String,
    open val title: String,
    open val description: String?,
    open val startTime: Long,
    open val remindAt: Long,
    open val cardType: AgendaItemType
) {
    data class Event(
        override val id: String,
        override val title: String,
        override val description: String,
        val from: Long,
        val to: Long,
        override val remindAt: Long,
        val host: String,
        val isUserEventCreator: Boolean,
        val attendees: List<Attendee>,
        val photos: List<Photo>
    ) : AgendaItem(id, title, description, startTime = from, remindAt, AgendaItemType.Event)

    data class Task(
        override val id: String,
        override val title: String,
        override val description: String?,
        val time: Long,
        override val remindAt: Long,
        val isDone: Boolean
    ) : AgendaItem(id, title, description, startTime = time, remindAt, AgendaItemType.Task)

    data class Reminder(
        override val id: String,
        override val title: String,
        override val description: String?,
        val time: Long,
        override val remindAt: Long
    ) : AgendaItem(id, title, description, startTime = time, remindAt, AgendaItemType.Reminder)
}
