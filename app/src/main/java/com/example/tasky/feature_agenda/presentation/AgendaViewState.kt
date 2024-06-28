package com.example.tasky.feature_agenda.presentation

import com.example.tasky.common.domain.error.DataError

sealed class AgendaViewState {
    data object LoadingSpinner : AgendaViewState()

    data class ErrorDialog(val dataError: DataError) : AgendaViewState()
}