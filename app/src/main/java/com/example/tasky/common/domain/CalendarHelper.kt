package com.example.tasky.common.domain

import com.example.tasky.feature_agenda.presentation.model.CalendarDay

interface CalendarHelper {
    fun getCalendarDaysForMonth(year: Int, month: Int): List<CalendarDay>
}