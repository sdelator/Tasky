package com.example.tasky.feature_agenda.presentation

import com.example.tasky.agenda_details.domain.model.Attendee
import com.example.tasky.agenda_details.domain.model.Photo
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.presentation.model.AgendaItemType
import com.example.tasky.common.presentation.util.toLong
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.LocalDate

/* define viewState as UI elements that change throughout app usage */
data class AgendaViewState(
    val datePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val showLogoutDropdown: Boolean = false,
    val showFabDropdown: Boolean = false,
    val monthSelected: Int = LocalDate.now().monthValue,
    val daySelected: Int = LocalDate.now().dayOfMonth,
    val calendarDays: List<CalendarDay> = emptyList(),
    val showLoadingSpinner: Boolean = false,
    val showErrorDialog: Boolean = false,
    val dataError: DataError = DataError.Network.UNKNOWN,
    val headerDateText: String = "",
    val yearSelected: Int = LocalDate.now().year,
    val dateSelected: Long = LocalDate.now().toLong(),
    val agendaItems: List<AgendaItem>? = null
)

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