package com.example.tasky.feature_agenda.presentation

import com.example.tasky.common.domain.error.DataError
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.LocalDate

// define viewState as something that can change throughout app usage
sealed class AgendaViewState(
    open val datePickerDialogState: MaterialDialogState = MaterialDialogState(),
    open val showDialog: Boolean = false,
    open val showLogoutDropdown: Boolean = false,
    open val monthSelected: Int = LocalDate.now().monthValue,
    open val daySelected: Int = LocalDate.now().dayOfMonth,
    open val calendarDays: List<CalendarDay> = emptyList()
) {
    data class Content(
        override val datePickerDialogState: MaterialDialogState = MaterialDialogState(),
        override val monthSelected: Int = LocalDate.now().monthValue,
        override val daySelected: Int = LocalDate.now().dayOfMonth,
        override val calendarDays: List<CalendarDay>, //todo set curent month days
        override val showLogoutDropdown: Boolean = false
    ) : AgendaViewState(
        monthSelected = monthSelected,
        daySelected = daySelected,
        calendarDays = calendarDays
    )
    data object LoadingSpinner : AgendaViewState()

    data class ErrorDialog(val dataError: DataError) : AgendaViewState(showDialog = true)
}