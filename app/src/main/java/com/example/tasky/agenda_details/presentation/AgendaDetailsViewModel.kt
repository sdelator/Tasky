package com.example.tasky.agenda_details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda_details.domain.ImageCompressor
import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.agenda_details.domain.model.EventDetails
import com.example.tasky.agenda_details.domain.model.Photo
import com.example.tasky.agenda_details.domain.repository.AgendaDetailsRemoteRepository
import com.example.tasky.agenda_details.presentation.utils.DateTimeHelper
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.model.AgendaItemType
import com.example.tasky.common.presentation.CardAction
import com.example.tasky.common.presentation.LineItemType
import com.example.tasky.common.presentation.ReminderTime
import com.example.tasky.common.presentation.util.toFormatted_HH_mm
import com.example.tasky.common.presentation.util.toFormatted_MMM_dd_yyyy
import com.vanpra.composematerialdialogs.MaterialDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AgendaDetailsViewModel @Inject constructor(
    private val imageCompressor: ImageCompressor,
    private val agendaDetailsRemoteRepository: AgendaDetailsRemoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val TAG = "AgendaDetailsViewModel"
        const val DEFAULT_TIME_RANGE = 15L
    }

    private val agendaItemId: String? = savedStateHandle.get<String>("agendaItemId")
    private val agendaItemAction: CardAction? = savedStateHandle.get<String>("cardAction")?.let {
        CardAction.valueOf(it)
    }
    private val agendaItemType = savedStateHandle.get<String>("agendaItemType")?.let {
        AgendaItemType.valueOf(it)
    }

    private val _viewState = MutableStateFlow(AgendaDetailsViewState())
    val viewState: StateFlow<AgendaDetailsViewState> = _viewState
    private val isNewEvent = (agendaItemAction == null)

    // viewEvent triggered by API response
    private val _viewEvent = Channel<AgendaDetailsViewEvent>()
    val viewEvent = _viewEvent.receiveAsFlow()

    init {
        when (agendaItemAction) {
            CardAction.Open -> {
                // api call to fetch data
                if (agendaItemType != null) {
                    loadDataForItem(agendaItemType)
                }
            }

            CardAction.Edit -> TODO()
            CardAction.Delete -> TODO()
            null -> {
                // isNewEvent
                _viewState.update {
                    it.copy(
                        fromDate = LocalDate.now().toFormatted_MMM_dd_yyyy(),
                        toDate = LocalDate.now().toFormatted_MMM_dd_yyyy(),
                        fromTime = LocalTime.now().toFormatted_HH_mm(),
                        toTime = LocalTime.now().plusMinutes(DEFAULT_TIME_RANGE).toFormatted_HH_mm()
                    )
                }
            }
        }
//        //todo make API call to fetch data
//        val sampleResponse =
//            AgendaItem.Event(
//                id = "a554ff78-4307-41fd-b2ec-01a96db98e00",
//                title = "pool party",
//                description = "bring snacks",
//                from = 1722502800,
//                to = 1722510000,
//                remindAt = 1722416400,
//                host = "666e55a7a3e6cb2e00e33a5f",
//                isUserEventCreator = true,
//                attendees = listOf(
//                    AttendeeDetails(
//                        email = "s@test.com",
//                        fullName = "sandra",
//                        userId = "666e55a7a3e6cb2e00e33a5f",
//                        eventId = "a554ff78-4307-41fd-b2ec-01a96db98e00",
//                        isGoing = true,
//                        remindAt = 1722416400
//                    )
//                ),
//                photos = listOf(
//                    Photo.RemotePhoto(
//                        key = "ca6a8dd2-c2a1-4a0e-b0f3-0681820dbd0d",
//                        url = "https://tasky-photos.s3.eu-central-1.amazonaws.com/ca6a8dd2-c2a1-4a0e-b0f3-0681820dbd0d?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240730T165744Z&X-Amz-SignedHeaders=host&X-Amz-Expires=518400&X-Amz-Credential=AKIAXEBKLPAR6LNGJAO5%2F20240730%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=bd34e8f4a81c1015656c6afcf08e62bc8c52043240380f5ff78c59a13f0b0bc6"
//                    ),
//                    Photo.RemotePhoto(
//                        key = "a27f5ccf-b474-4455-a63a-782f72594576",
//                        url = "https://tasky-photos.s3.eu-central-1.amazonaws.com/a27f5ccf-b474-4455-a63a-782f72594576?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20240730T165744Z&X-Amz-SignedHeaders=host&X-Amz-Expires=518400&X-Amz-Credential=AKIAXEBKLPAR6LNGJAO5%2F20240730%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=7d4d568f500d56562cdbefa127f3aa71aab32ecdbee66ce223de4b28db4eada3"
//                    )
//                )
//            )
//
//        _viewState.update {
//            it.copy(
//                title = sampleResponse.title,
//                description = sampleResponse.description,
//                toTime = DateTimeHelper.getLocalTimeFromEpoch(sampleResponse.to).toString(),
//                fromTime = DateTimeHelper.getLocalTimeFromEpoch(sampleResponse.from).toString(),
//                toDate = sampleResponse.to.convertMillisToMmmDdYyyy(),
//                fromDate = sampleResponse.from.convertMillisToMmmDdYyyy(),
//                photos = sampleResponse.photos.map { photo ->
//                    (photo as Photo.RemotePhoto)
//                    Photo.RemotePhoto(key = photo.key, url = photo.url)
//                },
//                reminderTime = getReminderTime(sampleResponse.remindAt, sampleResponse.from)
//            )
//        }
    }

    private fun loadDataForItem(agendaItemType: AgendaItemType) {
        when (agendaItemType) {
            AgendaItemType.Event -> loadDataForEvent()
            AgendaItemType.Task -> loadDataForTask()
            AgendaItemType.Reminder -> loadDataForReminder()
        }
    }

    private fun loadDataForEvent() {
        // todo
    }

    private fun loadDataForTask() {
        // todo
    }

    private fun loadDataForReminder() {
        // todo
    }

    private fun getReminderTime(remindAt: Long, fromTime: Long): ReminderTime {
        val secondsBetween = DateTimeHelper.calculateTimeDifferenceInSeconds(remindAt, fromTime)
        val reminderTime = getReminderTimeForDuration(secondsBetween)
        return reminderTime
    }

    private fun getReminderTimeForDuration(durationInSeconds: Long): ReminderTime {
        return ReminderTime.entries.find { it.epochSeconds == durationInSeconds }
            ?: ReminderTime.THIRTY_MINUTES
    }

    fun restoreViewState(title: String?, description: String?) {
        println("restore viewstate called")
        if (title != null) {
            _viewState.update {
                it.copy(
                    title = title
                )
            }
        }
        if (description != null) {
            _viewState.update {
                it.copy(
                    description = description
                )
            }
        }
    }

    fun edit() {
        //set viewstate to editing
    }

    fun delete() {
        //todo if no id (new item) then just navigate back
        if (agendaItemType == null) {
            throw IllegalArgumentException("agendaItemType is null")
        }
        when (agendaItemType) {
            AgendaItemType.Event -> deleteEvent()
            AgendaItemType.Task -> deleteTask()
            AgendaItemType.Reminder -> deleteReminder()
        }
    }

    private fun deleteEvent() {
        val eventId = "96faff73-8dba-43eb-a123-b32d15ab6395" //todo remove hard-coded
        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.deleteEvent(eventId = eventId)

            when (result) {
                is Result.Success -> {
                    println("event deleted!")
                    _viewEvent.send(AgendaDetailsViewEvent.NavigateToAgenda)
                }

                is Result.Error -> {
                    println("failed event deletion :(")
                    _viewState.update {
                        it.copy(
                            showLoadingSpinner = false,
                            showErrorDialog = true,
                            dataError = result.error
                        )
                    }
                }
            }
        }
    }

    private fun deleteTask() {
        val taskId = "372a6800-0b04-4828-b12d-280cfc55df12" //todo remove hard-coded
        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.deleteTask(taskId)

            when (result) {
                is Result.Success -> {
                    println("task deleted!")
                    _viewEvent.send(AgendaDetailsViewEvent.NavigateToAgenda)
                }

                is Result.Error -> {
                    println("failed task deletion :(")
                    _viewState.update {
                        it.copy(
                            showLoadingSpinner = false,
                            showErrorDialog = true,
                            dataError = result.error
                        )
                    }
                }
            }
        }
    }

    private fun deleteReminder() {
        val reminderId = "12345abcd" //todo remove hard-coded
        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.deleteReminder(reminderId)

            when (result) {
                is Result.Success -> {
                    println("reminder deleted!")
                    _viewEvent.send(AgendaDetailsViewEvent.NavigateToAgenda)
                }

                is Result.Error -> {
                    println("failed reminder deletion :(")
                    _viewState.update {
                        it.copy(
                            showLoadingSpinner = false,
                            showErrorDialog = true,
                            dataError = result.error
                        )
                    }
                }
            }
        }
    }

    fun save() {
        // todo save all items in local DB
        if (agendaItemType == null) {
            throw IllegalArgumentException("agendaItemType is null")
        }
        when (agendaItemType) {
            AgendaItemType.Event -> saveEvent()
            AgendaItemType.Task -> saveTask()
            AgendaItemType.Reminder -> saveReminder()
        }
    }

    private fun saveTask() {
        val atInEpochSeconds =
            DateTimeHelper.getEpochMillisecondsFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.createTask(
                taskDetails = AgendaItem.Task(
                    id = UUID.randomUUID().toString(),
                    title = _viewState.value.title ?: "",
                    description = _viewState.value.description ?: "",
                    time = atInEpochSeconds,
                    remindAt = atInEpochSeconds - reminderTime,
                    isDone = false //todo remove hardcode
                )
            )

            when (result) {
                is Result.Success -> {
                    println("success task creation!")
                    _viewEvent.send(AgendaDetailsViewEvent.NavigateToAgenda)
                }

                is Result.Error -> {
                    println("failed task creation :(")
                    _viewState.update {
                        it.copy(
                            showLoadingSpinner = false,
                            showErrorDialog = true,
                            dataError = result.error
                        )
                    }
                }
            }
        }
    }

    private fun saveReminder() {
        val atInEpochSeconds =
            DateTimeHelper.getEpochMillisecondsFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.createReminder(
                reminderDetails = AgendaItem.Reminder(
                    id = UUID.randomUUID().toString(),
                    title = _viewState.value.title ?: "",
                    description = _viewState.value.description ?: "",
                    time = atInEpochSeconds,
                    remindAt = atInEpochSeconds - reminderTime
                )
            )

            when (result) {
                is Result.Success -> {
                    println("success reminder creation!")
                    _viewEvent.send(AgendaDetailsViewEvent.NavigateToAgenda)
                }

                is Result.Error -> {
                    println("failed reminder creation :(")
                    _viewState.update {
                        it.copy(
                            showLoadingSpinner = false,
                            showErrorDialog = true,
                            dataError = result.error
                        )
                    }
                }
            }
        }
    }

    private fun saveEvent() {
        val fromInEpochSeconds =
            DateTimeHelper.getEpochMillisecondsFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val toInEpochSeconds =
            DateTimeHelper.getEpochMillisecondsFromDateAndTime(
                date = _viewState.value.toDate,
                time = _viewState.value.toTime
            )
        val reminderTime = _viewState.value.reminderTime.epochSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.createEvent(
                eventDetails = EventDetails(
                    id = UUID.randomUUID().toString(),
                    title = _viewState.value.title ?: "",
                    description = _viewState.value.description ?: "",
                    from = fromInEpochSeconds,
                    to = toInEpochSeconds,
                    remindAt = fromInEpochSeconds - reminderTime,
                    attendeeIds = listOf("a", "b")
                ),
                photosByteArray = getByteArrayFromPhotoList(_viewState.value.photos)
            )

            when (result) {
                is Result.Success -> {
                    println("success event creation!")
                    _viewEvent.send(AgendaDetailsViewEvent.NavigateToAgenda)
                    println("response = ${result.data}")
                }

                is Result.Error -> {
                    println("failed event creation :(")
                    _viewState.update {
                        it.copy(
                            showLoadingSpinner = false,
                            showErrorDialog = true,
                            dataError = result.error
                        )
                    }
                }
            }
        }
    }

    fun onDateSelected(
        date: LocalDate,
        dialogState: MaterialDialogState,
        lineItemType: LineItemType
    ) {
        when (lineItemType) {
            LineItemType.FROM, LineItemType.AT -> {
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
            LineItemType.FROM, LineItemType.AT -> {
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
        viewModelScope.launch {
            val localPhotos = compressPhoto(photoUris)
            displayPhotos(localPhotos)
        }
    }

    private suspend fun compressPhoto(uris: List<String>): List<Photo.LocalPhoto> {
        var skipped = 0
        val skippedIndexes = mutableListOf<Int>()
        val compressedByteArrayList = uris.mapIndexedNotNull { index, uri ->
            val drawable = imageCompressor.uriStringToDrawable(uri)
            if (drawable == null) {
                skipped++
                skippedIndexes.add(index)
                null
            } else {
                val compressedByteArray = imageCompressor.compressImage(drawable, quality = 80)
                if (compressedByteArray.size > 1024 * 1024) {
                    skipped++
                    skippedIndexes.add(index)
                    null
                } else {
                    compressedByteArray
                }
            }
        }

        // use original Uris list and remove the skipped ones out to pass into photoDetail screen
        val uriListFiltered = uris.filterIndexed { index, _ -> index !in skippedIndexes }

        // create a list of Photo.LocalPhoto objects
        val localPhotos = uriListFiltered.mapIndexed { index, uri ->
            Photo.LocalPhoto(uri = uri, byteArray = compressedByteArrayList[index])
        }

        _viewState.update {
            it.copy(
                photoSkipCount = skipped
            )
        }

        return localPhotos
    }

    private fun displayPhotos(photos: List<Photo>) {
        _viewState.update { it.copy(photos = it.photos + photos) }
    }

    fun getUrisOrUrlsFromPhotoList(photos: List<Photo>): List<String> {
        return photos.map { photo ->
            when (photo) {
                is Photo.LocalPhoto -> photo.uri
                is Photo.RemotePhoto -> photo.url
            }
        }
    }

    fun getByteArrayFromPhotoList(photos: List<Photo>): List<ByteArray> {
        return photos.filterIsInstance<Photo.LocalPhoto>()
            .map { it.byteArray }
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

    private fun deleteImage(photoUri: String?) {
        if (photoUri != null) {
            val oldPhotosList = _viewState.value.photos

            val imageIndex = oldPhotosList.indexOfFirst { photo ->
                when (photo) {
                    is Photo.LocalPhoto -> photo.uri == photoUri
                    is Photo.RemotePhoto -> photo.url == photoUri
                }
            }

            if (imageIndex != -1) {
                val newPhotosList = oldPhotosList.toMutableList().apply {
                    removeAt(imageIndex)
                }

                _viewState.update {
                    it.copy(
                        photos = newPhotosList,
                        photoUri = null
                    )
                }
            } // todo else { add error dialog if cant delete }
        }
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

    fun onErrorDialogDismissed() {
        _viewState.update {
            it.copy(showErrorDialog = false)
        }
    }
}