package com.example.tasky.feature_agenda.presentation

import com.example.tasky.common.domain.error.DataError

sealed class AgendaViewState {
    data object Success : AgendaViewState()
    data class Failure(val dataError: DataError) : AgendaViewState()
    data object Loading : AgendaViewState()

    data class ErrorDialog(val showErrorDialog: Boolean = false, val dialogMessage: String = "") :
        AgendaViewState()
}