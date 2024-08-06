package com.example.tasky.agenda_details.presentation

import com.example.tasky.R
import com.example.tasky.agenda_details.domain.model.AttendeeBasicInfoDetails
import com.example.tasky.agenda_details.domain.model.Photo
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.presentation.ReminderTime
import com.vanpra.composematerialdialogs.MaterialDialogState

data class AgendaDetailsViewState(
    val fromDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val toDatePickerDialogState: MaterialDialogState = MaterialDialogState(),
    val fromTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val toTimeDialogState: MaterialDialogState = MaterialDialogState(),
    val title: String? = null,
    val description: String? = null,
    val toTime: String = "",
    val fromTime: String = "",
    val toDate: String = "",
    val fromDate: String = "",
    val photos: List<Photo> = emptyList(),
    val reminderTime: ReminderTime = ReminderTime.THIRTY_MINUTES,
    val showReminderDropdown: Boolean = false,
    val attendeeFilterSelected: AttendeeFilter = AttendeeFilter.ALL,
    val photoSkipCount: Int = 0,
    val photoUri: String? = null,
    val showLoadingSpinner: Boolean = false,
    val showErrorDialog: Boolean = false,
    val dataError: DataError = DataError.Network.UNKNOWN,
    val isInEditMode: Boolean = false,
    val isAddVisitorDialogVisible: Boolean = false,
    val visitorList: List<AttendeeBasicInfoDetails> = emptyList(),
    val showVisitorDoesNotExist: Boolean = false
)

enum class AttendeeFilter(val typeName: Int) {
    ALL(R.string.all), GOING(R.string.going), NOT_GOING(R.string.not_going)
}
