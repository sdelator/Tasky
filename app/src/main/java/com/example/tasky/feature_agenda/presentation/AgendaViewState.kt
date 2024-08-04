package com.example.tasky.feature_agenda.presentation

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.presentation.util.toLong
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.LocalDate

/* define viewState as UI elements that change throughout app usage */
data class AgendaViewState(
    val datePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val showLogoutDropdown: Boolean = false,
    val showFabDropdown: Boolean = false,
    val showAgendaCardDropdown: Boolean = false,
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