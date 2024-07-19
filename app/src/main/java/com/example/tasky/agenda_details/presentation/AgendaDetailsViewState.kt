package com.example.tasky.agenda_details.presentation

import com.vanpra.composematerialdialogs.MaterialDialogState

data class AgendaDetailsViewState(
    val fromDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val toDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val fromTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val toTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val title: String = "",
    val description: String = "",
    val toTime: String = "",
    val fromTime: String = "",
    val toDate: String = "",
    val fromDate: String = "",
    val reminder: Int = 0,
    val attendeeFilterSelected: AttendeeFilter = AttendeeFilter.ALL
)

enum class AttendeeFilter(val typeName: String) {
    ALL("All"), GOING("Going"), NOT_GOING("Not going")
}
