package com.example.tasky.actions.presentation

data class ActionViewState(
    val actionTitle: String = "",
    val actionDescription: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val reminder: Int = 0,
    val actionViewMode: ActionViewMode = ActionViewMode.EDITABLE,
    val attendeeFilterSelected: AttendeeFilter = AttendeeFilter.ALL
)

enum class AttendeeFilter(type: String) {
    ALL("All"), GOING("Going"), NOT_GOING("Not Going")
}

enum class ActionViewMode {
    NON_EDITABLE, EDITABLE, ATTENDEE, ATTENDEE_EDITABLE
}
