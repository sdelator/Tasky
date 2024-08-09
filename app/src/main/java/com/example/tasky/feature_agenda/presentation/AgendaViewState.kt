package com.example.tasky.feature_agenda.presentation

import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.util.toEpochMillis
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.LocalDate
import java.time.ZonedDateTime

/* define viewState as UI elements that change throughout app usage */
data class AgendaViewState(
    val datePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val showLogoutDropdown: Boolean = false,
    val showFabDropdown: Boolean = false,
    val showAgendaCardDropdown: Boolean = false,
    val visibleAgendaCardDropdownId: String? = null,
    val monthSelected: Int = LocalDate.now().monthValue,
    val daySelected: Int = LocalDate.now().dayOfMonth,
    val calendarDays: List<CalendarDay> = emptyList(),
    val showLoadingSpinner: Boolean = false,
    val showErrorDialog: Boolean = false,
    val dataError: DataError = DataError.Network.UNKNOWN,
    val headerDateText: String = "",
    val yearSelected: Int = LocalDate.now().year,
    val dateSelected: Long = ZonedDateTime.now().toEpochMillis(),
    val agendaItems: List<AgendaItem>? = null,
    val needlePosition: ZonedDateTime = ZonedDateTime.now(),
    val isTaskChecked: Boolean = true,
    val taskCompleteForAgendaItemId: String = ""
)