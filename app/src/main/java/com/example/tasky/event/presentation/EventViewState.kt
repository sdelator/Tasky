package com.example.tasky.event.presentation

import com.vanpra.composematerialdialogs.MaterialDialogState

data class EventViewState(
    val datePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val timePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val eventName: String = "",
    val eventDescription: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val startDate: String = "",
    val endDate: String = "",
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
