package com.example.tasky.common.presentation.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalDate.toFormatted_MMMM_dd_yyyy(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
    return this.format(formatter)
}

fun LocalDate.toFormatted_MMM_dd_yyyy(): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy")
    return this.format(formatter)
}

fun LocalTime.toFormatted_HH_mm(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return this.format(formatter)
}