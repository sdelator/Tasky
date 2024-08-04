package com.example.tasky.agenda_details.presentation.utils


import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateTimeHelper {
    fun calculateTimeDifferenceInSeconds(startTime: Long, endTime: Long): Long {
        val startTimeInSeconds = Instant.ofEpochSecond(startTime)
        val endTimeInSeconds = Instant.ofEpochSecond(endTime)

        val duration = Duration.between(startTimeInSeconds, endTimeInSeconds)
        return duration.seconds
    }

    fun getEpochMillisecondsFromDateAndTime(date: String, time: String): Long {
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd yyyy")
        val localTime = LocalTime.parse(time)
        val localDate = LocalDate.parse(date, dateFormatter)
        val zdt = ZonedDateTime.of(localDate, localTime, ZoneOffset.UTC)
        return zdt.toEpochSecond() * 1000
    }

    fun formatEpochToDateString(epochMillis: Long): String {
        val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.of("UTC"))
        val formattedDateTime = DateTimeFormatter.ofPattern("MMM d, HH:mm").format(dateTime)
        return formattedDateTime
    }
}