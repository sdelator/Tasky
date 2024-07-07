package com.example.tasky.common.presentation.util

import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import java.time.LocalDate
import java.util.Locale

object CalendarHelper {
    fun getCalendarDaysForMonth(year: Int, month: Int): List<CalendarDay> {
        val firstDayOfMonth = LocalDate.of(year, month, 1)
        val daysInMonth = firstDayOfMonth.lengthOfMonth()

        val calendarDays = mutableListOf<CalendarDay>()
        for (date in 1..daysInMonth) {
            val currentDate = firstDayOfMonth.withDayOfMonth(date)
            val dayOfWeek = currentDate.dayOfWeek.getDisplayName(
                java.time.format.TextStyle.NARROW,
                Locale.getDefault()
            )
            calendarDays.add(CalendarDay(dayOfWeek, date))
        }

        return calendarDays
    }
}