package com.example.tasky.event.presentation

import com.vanpra.composematerialdialogs.MaterialDialogState

data class EventViewState(
    val fromDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val toDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val fromTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val toTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val eventName: String = "",
    val eventDescription: String = "",
    val fromTime: String = "",
    val toTime: String = "",
    val fromDate: String = "",
    val toDate: String = "",
    val reminder: Int = 0,
    val eventViewMode: EventViewMode = EventViewMode.EDITABLE,
    val filterSelected: Filter = Filter.ALL
)

enum class Filter(type: String) {
    ALL("All"), GOING("Going"), NOT_GOING("Not Going")
}

enum class EventViewMode {
    NON_EDITABLE, EDITABLE, ATTENDEE, ATTENDEE_EDITABLE
}
