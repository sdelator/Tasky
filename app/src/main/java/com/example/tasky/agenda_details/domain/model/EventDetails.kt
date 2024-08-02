package com.example.tasky.agenda_details.domain.model

data class EventDetails(
    val id: String,
    val title: String,
    val description: String,
    val from: Long,
    val to: Long,
    val remindAt: Long,
    val attendeeIds: List<String>
)

sealed class Photo(open val uri: String) {
    data class LocalPhoto(val byteArray: ByteArray, override val uri: String) : Photo(uri = uri)

    data class RemotePhoto(val key: String, val url: String) : Photo(uri = url)
}