package com.example.tasky.agenda_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda_details.data.model.Attendee
import com.example.tasky.agenda_details.data.model.EventResponse
import com.example.tasky.agenda_details.data.model.Photo
import com.example.tasky.agenda_details.data.model.PhotoType
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
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
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
    private val isNewEvent = true

    init {
        if (isNewEvent) {
            _viewState.update {
                it.copy(
                    fromDate = LocalDate.now().toFormatted_MMM_dd_yyyy(),
                    toDate = LocalDate.now().toFormatted_MMM_dd_yyyy(),
                    fromTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                        .toString(),
                    toTime = LocalTime.now().plusMinutes(DEFAULT_TIME_RANGE)
                        .format(DateTimeFormatter.ofPattern("HH:mm")).toString()
                )
            }
        } else {
            //todo make API call to fetch data
            val sampleResponse =
                EventResponse(
                    id = "a554ff78-4307-41fd-b2ec-01a96db98e00",
                    title = "pool party",
                    description = "bring snacks",
                    from = 1722502800,
                    to = 1722510000,
                    remindAt = 1722416400,
                    host = "666e55a7a3e6cb2e00e33a5f",
                    isUserEventCreator = true,
                    attendees = listOf(
                        Attendee(
                            email = "s@test.com",
                            fullName = "sandra",
                            userId = "666e55a7a3e6cb2e00e33a5f",
                            eventId = "a554ff78-4307-41fd-b2ec-01a96db98e00",
                            isGoing = true,
                            remindAt = 1722416400
                        )
                    ),
                    photos = listOf(
                        Photo(
                            key = "ca6a8dd2-c2a1-4a0e-b0f3-0681820dbd0d",
                            url = "https://tasky-photos.s3.eu-central-1.amazonaws.com/ca6a8dd2-c2a1-4a0e-b0f3-0681820dbd0d?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240730T165744Z&X-Amz-SignedHeaders=host&X-Amz-Expires=518400&X-Amz-Credential=AKIAXEBKLPAR6LNGJAO5%2F20240730%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=bd34e8f4a81c1015656c6afcf08e62bc8c52043240380f5ff78c59a13f0b0bc6"
                        ),
                        Photo(
                            key = "a27f5ccf-b474-4455-a63a-782f72594576",
                            url = "https://tasky-photos.s3.eu-central-1.amazonaws.com/a27f5ccf-b474-4455-a63a-782f72594576?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240730T165744Z&X-Amz-SignedHeaders=host&X-Amz-Expires=518400&X-Amz-Credential=AKIAXEBKLPAR6LNGJAO5%2F20240730%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=7d4d568f500d56562cdbefa127f3aa71aab32ecdbee66ce223de4b28db4eada3"
                        )
                    )
                )

            _viewState.update {
                it.copy(
                    title = sampleResponse.title,
                    description = sampleResponse.description,
                    toTime = getLocalTimeFromEpoch(sampleResponse.to).toString(),
                    fromTime = getLocalTimeFromEpoch(sampleResponse.from).toString(),
                    toDate = getLocalDateFromEpoch(sampleResponse.to).toFormatted_MMM_dd_yyyy(),
                    fromDate = getLocalDateFromEpoch(sampleResponse.from).toFormatted_MMM_dd_yyyy(),
                    photos = sampleResponse.photos.map { photo ->
                        PhotoType.RemotePhoto(key = photo.key, url = photo.url)
                    },
                    reminderTime = getReminderTime(sampleResponse.remindAt, sampleResponse.from)
                )
            }
        }
    }

    private fun getReminderTime(remindAt: Long, fromTime: Long): ReminderTime {
        val secondsBetween = calculateTimeDifferenceInSeconds(remindAt, fromTime)
        return getReminderTimeForDuration(secondsBetween)
    }

    private fun getReminderTimeForDuration(durationInSeconds: Long): ReminderTime {
        return ReminderTime.entries.find { it.epochSeconds == durationInSeconds }
            ?: ReminderTime.THIRTY_MINUTES
    }

    private fun calculateTimeDifferenceInSeconds(epochReminderTime: Long, fromTime: Long): Long {
        val instant1 = Instant.ofEpochSecond(epochReminderTime)
        val instant2 = Instant.ofEpochSecond(fromTime)

        val duration = Duration.between(instant1, instant2)
        return duration.seconds
    }

    private fun getLocalDateFromEpoch(
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

    fun resetPhotoSkipCount() {
        _viewState.update {
            it.copy(
                photoSkipCount = 0
            )
        }
    }

    fun onAddPhotosClick(photoUris: List<String>) {
        // todo conversion ? to photo
        viewModelScope.launch {
            val localPhotos = compressPhoto(photoUris)
            displayPhotos(localPhotos)
        }
    }

    private suspend fun compressPhoto(uris: List<String>): List<PhotoType.LocalPhoto> {
        var skipped = 0
        val skippedIndexes = mutableListOf<Int>()
        val compressedByteArrayList = uris.mapIndexedNotNull { index, uri ->
            val drawable = imageCompressor.uriStringToDrawable(uri)
            val compressedByteArray = imageCompressor.compressImage(drawable, quality = 80)
            if (compressedByteArray.size > 1024 * 1024) {
                skipped++
                skippedIndexes.add(index)
                null
            } else {
                compressedByteArray
            }
        }

        // use original Uris list and remove the skipped ones out to pass into photoDetail screen
        val uriListFiltered = uris.filterIndexed { index, _ -> index !in skippedIndexes }

        // create a list of Photo.LocalPhoto objects
        val localPhotos = uriListFiltered.mapIndexed { index, uri ->
            PhotoType.LocalPhoto(uri = uri, byteArray = compressedByteArrayList[index])
        }

        _viewState.update {
            it.copy(
                photoSkipCount = skipped
            )
        }

        return localPhotos
    }

    private fun displayPhotos(photos: List<PhotoType>) {
        _viewState.update { it.copy(photos = it.photos + photos) }
    }

    fun getUrisOrUrlsFromPhotoList(photos: List<PhotoType>): List<String> {
        return photos.map { photo ->
            when (photo) {
                is PhotoType.LocalPhoto -> photo.uri
                is PhotoType.RemotePhoto -> photo.url
            }
        }
    }

    fun setSelectedImage(photoUri: String) {
        _viewState.update {
            it.copy(
                photoUri = photoUri
            )
        }
    }

    private fun resetImageSelected() {
        _viewState.update {
            it.copy(
                photoUri = null
            )
        }
    }

    private fun deleteImage(photoUri: String?) {  // TODO add an error if cannot be deleted
//        if (photoUri != null) {
//            // get imageUriList and remove uri from there
//            val oldUriList = _viewState.value.uriImageList
//            val imageIndex = oldUriList.indexOf(photoUri)
//            val newUriList = oldUriList.toMutableList().apply {
//                removeAt(imageIndex)
//            }
//
//            // update compressedImageList
//            val oldImageList = _viewState.value.byteArrayImageList
//            val newImageList = oldImageList.toMutableList().apply {
//                removeAt(imageIndex)
//            }
//
//            _viewState.update {
//                it.copy(
//                    uriImageList = newUriList,
//                    byteArrayImageList = newImageList,
//                    photoUri = null
//                )
//            }
//        }
    }

    fun handlePhotoDetailAction(action: PhotoDetailAction) {
        when (action) {
            PhotoDetailAction.ERASE -> {
                deleteImage(_viewState.value.photoUri)
            }

            PhotoDetailAction.CANCEL -> {
                resetImageSelected()
            }

            PhotoDetailAction.NONE -> println("no image action")
        }
    }
}