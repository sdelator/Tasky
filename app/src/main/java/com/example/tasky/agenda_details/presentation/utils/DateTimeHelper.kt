package com.example.tasky.agenda_details.presentation.utils

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateTimeHelper {
    fun getLocalDateFromEpoch(
        epochSeconds: Long,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): LocalDate {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
        return zonedDateTime.toLocalDate()
    }

    fun getLocalTimeFromEpoch(
        epochSeconds: Long,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): LocalTime {
        val instant = Instant.ofEpochSecond(epochSeconds)
        val zonedDateTime = ZonedDateTime.ofInstant(instant, zoneId)
        return zonedDateTime.toLocalTime()
    }

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
        val dateFormat = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
        val date = Date(epochMillis)
        return dateFormat.format(date)
    }
}