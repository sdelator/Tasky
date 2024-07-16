package com.example.tasky.common.presentation.util

import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun LocalDate.toFormatted_MM_DD_YYYY(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
    return this.format(formatter)
}

fun LocalDate.toLong(): Long {
    return this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
}