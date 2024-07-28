package com.example.tasky.agenda_details.presentation

import android.net.Uri
import com.example.tasky.R
import com.example.tasky.common.presentation.ReminderTime
import com.vanpra.composematerialdialogs.MaterialDialogState

data class AgendaDetailsViewState(
    val fromDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val toDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val fromTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val toTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val title: String = "",
    val description: String = "",
    val toTime: String = "",
    val fromTime: String = "",
    val toDate: String = "",
    val fromDate: String = "",
    val uriImageList: List<Uri?> = emptyList(),
    val byteArrayImageList: List<ByteArray?> = emptyList(),
    val reminderTime: ReminderTime = ReminderTime.THIRTY_MINUTES,
    val showReminderDropdown: Boolean = false,
    val attendeeFilterSelected: AttendeeFilter = AttendeeFilter.ALL,
    val photoSkipCount: Int = 0,
    val photoUri: Uri? = null
)

enum class AttendeeFilter(val typeName: Int) {
    ALL(R.string.all), GOING(R.string.going), NOT_GOING(R.string.not_going)
}
