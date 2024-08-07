package com.example.tasky.agenda_details.presentation.utils


import com.example.tasky.common.domain.util.toEpochMillis
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateTimeHelper {
    fun calculateRemindAtMs(timeInZonedDT: ZonedDateTime, reminderTimeMillis: Long): Long {
        val startTime = timeInZonedDT.toEpochMillis()
        val remindAt = startTime - reminderTimeMillis
        return remindAt
    }

    fun calculateRemindAtZDT(
        timeInZonedDT: ZonedDateTime,
        reminderTimeMillis: Long
    ): ZonedDateTime {
        val reminderDuration = Duration.ofMillis(reminderTimeMillis)
        val remindAt = timeInZonedDT.minus(reminderDuration)
        return remindAt
    }

    fun getZonedDateTimeFromDateAndTime(date: String, time: String): ZonedDateTime {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm")
        val datePart = LocalDateTime.parse("$date $time", dateTimeFormatter)
        return datePart.atZone(ZoneId.systemDefault())
    }
}