package com.example.tasky.agenda_details.presentation

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda_details.domain.ImageCompressionUseCase
import com.example.tasky.common.presentation.LineItemType
import com.example.tasky.common.presentation.ReminderTime
import com.example.tasky.common.presentation.util.toFormatted_MMM_dd_yyyy
import com.vanpra.composematerialdialogs.MaterialDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AgendaDetailsViewModel @Inject constructor(
    private val imageCompressionUseCase: ImageCompressionUseCase
) : ViewModel() {

    companion object {
        const val TAG = "AgendaDetailsViewModel"
        const val DEFAULT_TIME_RANGE = 15L
    }

    private val _viewState = MutableStateFlow(AgendaDetailsViewState())
    val viewState: StateFlow<AgendaDetailsViewState> = _viewState

    init {
        _viewState.update {
            it.copy(
                fromDate = LocalDate.now().toFormatted_MMM_dd_yyyy(),
                toDate = LocalDate.now().toFormatted_MMM_dd_yyyy(),
                fromTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
                toTime = LocalTime.now().plusMinutes(DEFAULT_TIME_RANGE)
                    .format(DateTimeFormatter.ofPattern("HH:mm")).toString()
            )
        }
    }

    fun cancel() {
    }

    fun save() {
    }

    fun onDateSelected(
        date: LocalDate,
        dialogState: MaterialDialogState,
        lineItemType: LineItemType
    ) {
        when (lineItemType) {
            LineItemType.FROM -> {
                _viewState.update {
                    it.copy(
                        fromDatePickerDialogState = dialogState,
                        fromDate = date.toFormatted_MMM_dd_yyyy()
                    )
                }
            }

            LineItemType.TO -> {
                _viewState.update {
                    it.copy(
                        toDatePickerDialogState = dialogState,
                        toDate = date.toFormatted_MMM_dd_yyyy()
                    )
                }
            }
        }
    }

    fun onTimeSelected(
        time: LocalTime,
        lineItemType: LineItemType,
        dialogState: MaterialDialogState
    ) {
        when (lineItemType) {
            LineItemType.FROM -> {
                _viewState.update {
                    it.copy(
                        fromTimeDialogState = dialogState,
                        fromTime = time.toString()
                    )
                }
            }

            LineItemType.TO -> {
                _viewState.update {
                    it.copy(
                        toTimeDialogState = dialogState,
                        toTime = time.toString()
                    )
                }
            }
        }
    }

    fun toggleReminderDropdownVisibility() {
        _viewState.update {
            it.copy(showReminderDropdown = !_viewState.value.showReminderDropdown)
        }
    }

    fun setReminder(reminderTime: ReminderTime) {
        // TODO actually set the timer
        _viewState.update {
            it.copy(
                reminderTime = reminderTime,
                showReminderDropdown = false
            )
        }
    }

    fun setAttendeeFilter(attendeeFilter: AttendeeFilter) {
        // TODO add filter logic
        _viewState.update {
            it.copy(
                attendeeFilterSelected = attendeeFilter
            )
        }
    }

    fun onAddPhotosClick(photoUris: List<Uri>) {
        _viewState.update {
            it.copy(
                photosUri = photoUris
            )
        }
    }

    fun compressAndAddImage(context: Context, uris: List<Uri>) {
        viewModelScope.launch {
            var skipped = 0
            val newCompressedList = uris.mapNotNull { uri ->
                val drawable = imageCompressionUseCase.uriToDrawable(context, uri)
                val originalBitmap = (drawable as BitmapDrawable).bitmap
                Log.d(TAG, "Original Bitmap Size in bytes: ${originalBitmap.byteCount}")

                val compressedByteArray = imageCompressionUseCase.compressImage(drawable)

                val compressedBitmap =
                    BitmapFactory.decodeByteArray(compressedByteArray, 0, compressedByteArray.size)
                Log.d(TAG, "Compressed Bitmap Size in bytes: ${compressedByteArray.size}")

                if (compressedByteArray.size > 1024 * 1024) {
                    skipped++
                    null
                } else {
                    compressedBitmap
                }
            }

            _viewState.update {
                it.copy(
                    compressedImages = it.compressedImages + newCompressedList,
                    photoSkipCount = skipped
                )
            }
        }
    }
}