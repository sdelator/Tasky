package com.example.tasky.agenda_details.presentation

import com.vanpra.composematerialdialogs.MaterialDialogState

data class AgendaDetailsViewState(
    val fromDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val toDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val fromTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val toTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val actionTitle: String = "",
    val actionDescription: String = "",
    val toTime: String = "",
    val fromTime: String = "",
    val toDate: String = "",
    val fromDate: String = "",
    val reminder: Int = 0,
    val agendaDetailsViewMode: AgendaDetailsViewMode = AgendaDetailsViewMode.EDITABLE,
    val attendeeFilterSelected: AttendeeFilter = AttendeeFilter.ALL
)

enum class AttendeeFilter(type: String) {
    ALL("All"), GOING("Going"), NOT_GOING("Not Going")
}

enum class AgendaDetailsViewMode {
    NON_EDITABLE, EDITABLE, ATTENDEE, ATTENDEE_EDITABLE
}
