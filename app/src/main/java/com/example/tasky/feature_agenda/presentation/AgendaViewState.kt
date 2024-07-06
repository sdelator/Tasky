package com.example.tasky.feature_agenda.presentation

import com.example.tasky.common.domain.error.DataError

// define viewState as something that can change throughout app usage
sealed class AgendaViewState(
    open val showDialog: Boolean = false,
    open val showLogoutDropdown: Boolean = false,
//    monthSelected: Int = LocalDate.now().monthValue,
//    daySelected: Int =
) {
    data class Content(
        override val showLogoutDropdown: Boolean = false
    ) : AgendaViewState()
    data object LoadingSpinner : AgendaViewState()

    data class ErrorDialog(val dataError: DataError) : AgendaViewState(showDialog = true)
}