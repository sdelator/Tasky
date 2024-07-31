package com.example.tasky.agenda_details.presentation

sealed class AgendaDetailsViewEvent {
    data object NavigateToAgenda : AgendaDetailsViewEvent()
}