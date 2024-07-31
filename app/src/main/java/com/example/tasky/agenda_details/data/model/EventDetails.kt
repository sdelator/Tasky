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