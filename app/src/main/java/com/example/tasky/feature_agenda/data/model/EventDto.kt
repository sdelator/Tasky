package com.example.tasky.feature_agenda.data.model

import com.example.tasky.agenda_details.domain.model.Photo

data class EventDto(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<AttendeeDto>,
    val photos: List<Photo.RemotePhoto>
)

data class AttendeeDto(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long
)

data class AttendeeAccountDto(
    val doesUserExist: Boolean,
    val attendee: AttendeeBasicInfoDto?
)

data class AttendeeBasicInfoDto(
    val email: String,
    val fullName: String,
    val userId: String
)