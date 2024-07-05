package com.example.tasky.common.data

import com.example.tasky.common.domain.CalendarHelper
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarHelperImpl @Inject constructor() : CalendarHelper {
    override fun getCalendarDaysForMonth(year: Int, month: Int): List<CalendarDay> {
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