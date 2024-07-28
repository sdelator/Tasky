package com.example.tasky.agenda_details.presentation

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda_details.domain.ImageCompressor
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
    private val imageCompressor: ImageCompressor
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
        // todo save all items in local DB and server
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
        compressAndAddImage(photoUris)
    }

    private fun compressAndAddImage(uris: List<Uri>) {
        viewModelScope.launch {
            var skipped = 0
            val skippedIndexes = mutableListOf<Int>()
            val newCompressedList = uris.mapIndexedNotNull { index, uri ->
                val drawable = imageCompressor.uriToDrawable(uri)
                val originalBitmap = (drawable as BitmapDrawable).bitmap
                Log.d(TAG, "Original Bitmap Size in bytes: ${originalBitmap.byteCount}")

                val compressedByteArray = imageCompressor.compressImage(drawable)
                Log.d(TAG, "Compressed Bitmap Size in bytes: ${compressedByteArray.size}")

                if (compressedByteArray.size > 1024 * 1024) {
                    skipped++
                    skippedIndexes.add(index)
                    null
                } else {
                    compressedByteArray
                }
            }

            //use original Uris list and remove the skipped ones out to pass into photoDetail screen
            val uriListFiltered = uris.filterIndexed { index, _ -> index !in skippedIndexes }

            _viewState.update {
                it.copy(
                    uriListFiltered = uriListFiltered,
                    compressedImages = it.compressedImages + newCompressedList,
                    photoSkipCount = skipped
                )
            }
        }
    }

    fun getImageUri(byteArray: ByteArray) {
        //todo get byteArray index
        // if its a newly created event, use original uri list (if null (user deleted before saving) then print error
        // else get uri from local db/remote repository

        val index = findByteArrayIndex(_viewState.value.compressedImages, byteArray)
        val photoUri =
            _viewState.value.uriListFiltered[index] //con: how do I delete uri from details screen?

        _viewState.update {
            it.copy(
                photoUri = photoUri
            )
        }
    }

    private fun findByteArrayIndex(list: List<ByteArray?>, target: ByteArray): Int {
        return list.indexOfFirst { it != null && it.contentEquals(target) }
    }

//    fun convertImageToString(byteArray: ByteArray) {
//        viewModelScope.launch {
//            val imageString = imageCompressor.byteArrayToString(byteArray)
//            _viewState.update {
//                it.copy(
//                    imageDetail = imageString
//                )
//            }
//        }
//    }

    private fun resetImageDetail() {
        _viewState.update {
            it.copy(
                photoUri = null
            )
        }
    }

    private fun deleteImage(photoUri: Uri?) {  // TODO add an error if cannot be deleted
        if (photoUri != null) {
            // get imageUriList and remove uri from there
            val oldUriList = _viewState.value.uriListFiltered
            val imageIndex = oldUriList.indexOf(photoUri)
            val newUriList = oldUriList.filter { it != photoUri }

            // update compressedImageList
            val oldImageList = _viewState.value.compressedImages
            val newImageList = oldImageList.toMutableList().apply {
                removeAt(imageIndex)
            }

            _viewState.update {
                it.copy(
                    uriListFiltered = newUriList,
                    compressedImages = newImageList,
                    photoUri = null
                )
            }

//            viewModelScope.launch {
//                val imageString = imageCompressor.stringToByteArray(image)
//
//                val oldImageList = _viewState.value.compressedImages
//                val newImageList =
//                    oldImageList.filterNot { it?.contentEquals(imageString) ?: false }
//                        .toMutableList()
//
//                _viewState.update {
//                    it.copy(
//                        compressedImages = newImageList,
//                        photoUri = null
//                    )
//                }
//            }
        }
    }

    fun handlePhotoDetailAction(action: PhotoDetailAction) {
        viewModelScope.launch {
            when (action) {
                PhotoDetailAction.ERASE -> {
                    //TODO deleteImage(_viewState.value.photoUri)
                    deleteImage(_viewState.value.photoUri)
                }

                PhotoDetailAction.CANCEL -> {
                    resetImageDetail()
                }

                PhotoDetailAction.NONE -> println("no image action")
            }
        }
    }
}