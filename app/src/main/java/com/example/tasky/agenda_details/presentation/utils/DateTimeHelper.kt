package com.example.tasky.agenda_details.presentation.utils


import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateTimeHelper {
    fun calculateTimeDifferenceInSeconds(startTime: Long, endTime: Long): Long {
        val startTimeInSeconds = Instant.ofEpochSecond(startTime)
        val endTimeInSeconds = Instant.ofEpochSecond(endTime)

        val duration = Duration.between(startTimeInSeconds, endTimeInSeconds)
        return duration.seconds
    }

    fun getZonedDateTimeFromDateAndTime(date: String, time: String): ZonedDateTime {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm")
        val datePart = LocalDateTime.parse("$date $time", dateTimeFormatter)
        return datePart.atZone(ZoneId.systemDefault())
    }
}