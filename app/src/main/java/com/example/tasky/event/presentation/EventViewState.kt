package com.example.tasky.event.presentation

data class EventViewState(
    val eventName: String = "",
    val eventDescription: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val reminder: Int = 0,
    val eventViewMode: EventViewMode = EventViewMode.EDITABLE,
    val attendeeFilterSelected: AttendeeFilter = AttendeeFilter.ALL
)

enum class AttendeeFilter(type: String) {
    ALL("All"), GOING("Going"), NOT_GOING("Not Going")
}

enum class EventViewMode {
    NON_EDITABLE, EDITABLE, ATTENDEE, ATTENDEE_EDITABLE
}
