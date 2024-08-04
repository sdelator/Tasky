package com.example.tasky.common.domain.util

import java.time.Instant
import java.time.LocalDate
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

fun Long.convertMillisToDateDdMmmYyyy(): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter.ofPattern("dd MMM yyyy").format(dateTime)
    return formattedDateTime
}

fun Long.convertMillisToLocalDate(): LocalDate {
    val zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
    return zonedDateTime.toLocalDate()
}

fun Long.convertMillisToMmmDdYyyy(): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
    val formattedDateTime = DateTimeFormatter.ofPattern("MMM dd yyyy").format(dateTime)
    return formattedDateTime
}

fun Int.convertMonthToString(): String {
    return java.time.Month.of(this).getDisplayName(TextStyle.FULL, Locale.getDefault()).uppercase()
}