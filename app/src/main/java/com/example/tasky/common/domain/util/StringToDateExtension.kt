package com.example.tasky.common.domain.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun Long.convertMillisToMonth(): String {
    val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    return dateFormat.format(Date(this))
}

fun Long.convertMillisToDate(): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.of("UTC"))
    val formattedDateTime = DateTimeFormatter.ofPattern("dd MMM yyyy").format(dateTime)
    return formattedDateTime
}