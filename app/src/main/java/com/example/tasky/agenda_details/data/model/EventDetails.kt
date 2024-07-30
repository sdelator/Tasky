package com.example.tasky.agenda_details.data.model

data class EventDetails(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeeIds: List<String>
)

sealed class PhotoType {
    data class LocalPhoto(
        val byteArray: ByteArray,
        val uri: String
    ) : PhotoType()

    data class RemotePhoto(val key: String, val url: String) : PhotoType()
}

data class EventResponse(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val host: String,
    val isUserEventCreator: Boolean,
    val attendees: List<Attendee>,
    val photos: List<Photo>
)

data class Attendee(
    val email: String,
    val fullName: String,
    val userId: String,
    val eventId: String,
    val isGoing: Boolean,
    val remindAt: Long
)

data class Photo(
    val key: String,
    val url: String
)