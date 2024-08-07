package com.example.tasky.common.domain.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun Long.convertMillisToMmmm(): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter.ofPattern("MMMM").format(dateTime)
    return formattedDateTime
}

fun ZonedDateTime.toHHmmString(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}

fun Long.convertMillisToDateDdMmmYyyy(): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter.ofPattern("dd MMM yyyy").format(dateTime)
    return formattedDateTime
}

fun Long.convertMillisToZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

fun ZonedDateTime.toMillisToMmmDdYyyy(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy")
    return this.format(formatter)
}

fun ZonedDateTime.toMMMdHHmm(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")
    return this.format(formatter)
}


fun Int.convertMonthToString(): String {
    return java.time.Month.of(this).getDisplayName(TextStyle.FULL, Locale.getDefault()).uppercase()
}

fun ZonedDateTime.toEpochMillis(): Long {
    return this.toInstant().toEpochMilli()
}

fun LocalDate.getZonedDateTime(): ZonedDateTime {
    val localTime = LocalTime.MIDNIGHT
    val localDateTime = LocalDateTime.of(this, localTime)
    return ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
}