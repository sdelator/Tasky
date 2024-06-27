package com.example.tasky.feature_agenda.presentation

import com.example.tasky.common.domain.error.DataError

sealed class AgendaViewState {
    object Success : AgendaViewState()
    data class Failure(val dataError: DataError) : AgendaViewState()
    object Loading : AgendaViewState()
}