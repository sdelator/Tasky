package com.example.tasky.feature_agenda.presentation

sealed class AgendaViewEvent {
    data object NavigateToLoginScreen : AgendaViewEvent()
}