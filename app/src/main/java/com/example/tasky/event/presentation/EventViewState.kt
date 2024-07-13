package com.example.tasky.event.presentation

data class EventViewState(
    val eventName: String,
    val eventDescription: String,
    val startTime: String,
    val endTime: String,
    val startDate: String,
    val endDate: String,
    val reminder: Int,
    val filterSelected: Filter
)

enum class Filter(type: String) {
    ALL("All"), GOING("Going"), NOT_GOING("Not Going")
}
