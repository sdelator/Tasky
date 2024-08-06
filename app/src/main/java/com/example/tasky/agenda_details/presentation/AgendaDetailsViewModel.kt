package com.example.tasky.agenda_details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.agenda_details.domain.ImageCompressor
import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.agenda_details.domain.model.AttendeeBasicInfoDetails
import com.example.tasky.agenda_details.domain.model.EventDetails
import com.example.tasky.agenda_details.domain.model.EventDetailsUpdated
import com.example.tasky.agenda_details.domain.model.Photo
import com.example.tasky.agenda_details.domain.repository.AgendaDetailsRemoteRepository
import com.example.tasky.agenda_details.presentation.utils.DateTimeHelper
import com.example.tasky.common.domain.Result
import com.example.tasky.common.domain.SessionStateManager
import com.example.tasky.common.domain.model.AgendaItemType
import com.example.tasky.common.domain.util.EmailPatternValidatorImpl
import com.example.tasky.common.domain.util.toEpochMillis
import com.example.tasky.common.domain.util.toHHmmString
import com.example.tasky.common.domain.util.toMillisToMmmDdYyyy
import com.example.tasky.common.presentation.CardAction
import com.example.tasky.common.presentation.LineItemType
import com.example.tasky.common.presentation.ReminderTime
import com.example.tasky.common.presentation.util.ProfileUtils
import com.example.tasky.common.presentation.util.toFormatted_HH_mm
import com.example.tasky.common.presentation.util.toFormatted_MMM_dd_yyyy
import com.vanpra.composematerialdialogs.MaterialDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AgendaDetailsViewModel @Inject constructor(
    private val emailPatternValidator: EmailPatternValidatorImpl,
    private val imageCompressor: ImageCompressor,
    private val agendaDetailsRemoteRepository: AgendaDetailsRemoteRepository,
    private val sessionStateManager: SessionStateManager,
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
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    val isEmailValid = _email.map { email ->
        emailPatternValidator.isValidEmailPattern(email) // <- Each email emission is mapped to this boolean when it changes
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    private val _viewState = MutableStateFlow(AgendaDetailsViewState())
    val viewState: StateFlow<AgendaDetailsViewState> = _viewState

    // viewEvent triggered by API response
    private val _viewEvent = Channel<AgendaDetailsViewEvent>()
    val viewEvent = _viewEvent.receiveAsFlow()

    init {
        when (agendaItemAction) {
            CardAction.Open -> loadDataForAgendaItem(isEditMode = false)
            CardAction.Edit -> loadDataForAgendaItem(isEditMode = true)
            CardAction.Delete -> deleteAgendaItem()
            null -> {
                // isNewEvent
                viewModelScope.launch {
                    _viewState.update {
                        it.copy(
                            fromDate = LocalDate.now().toFormatted_MMM_dd_yyyy(),
                            toDate = LocalDate.now().toFormatted_MMM_dd_yyyy(),
                            fromTime = LocalTime.now().toFormatted_HH_mm(),
                            toTime = LocalTime.now().plusMinutes(DEFAULT_TIME_RANGE)
                                .toFormatted_HH_mm(),
                            isInEditMode = true,
                            visitorGoingList = listOf(
                                AttendeeBasicInfoDetails(
                                    email = "",
                                    fullName = sessionStateManager.getName()!!,
                                    userId = sessionStateManager.getUserId().first().toString(),
                                    userInitials = sessionStateManager.getName()
                                        ?.let { ProfileUtils.getInitials(it) } ?: "",
                                    isCreator = true
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun activateEditModeForAgendaItem() {
        _viewState.update {
            it.copy(isInEditMode = true)
        }
    }

    private fun loadDataForAgendaItem(isEditMode: Boolean) {
        if (agendaItemType == null) {
            throw IllegalArgumentException("agendaItemType is null")
        }
        when (agendaItemType) {
            AgendaItemType.Event -> loadDataForEvent(isEditMode)
            AgendaItemType.Task -> loadDataForTask(isEditMode)
            AgendaItemType.Reminder -> loadDataForReminder(isEditMode)
        }
    }

    private fun loadDataForEvent(isEditMode: Boolean) {
        if (agendaItemId == null) {
            throw IllegalArgumentException("agendaItemId is null")
        }

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.loadEvent(eventId = agendaItemId)

            when (result) {
                is Result.Success -> {
                    println("event loaded!")
                    _viewState.update {
                        it.copy(
                            title = result.data.title,
                            description = result.data.description,
                            fromTime = result.data.from.toHHmmString(),
                            fromDate = result.data.from.toMillisToMmmDdYyyy(),
                            toTime = result.data.to.toHHmmString(),
                            toDate = result.data.to.toMillisToMmmDdYyyy(),
                            reminderTime = getReminderTime(result.data.remindAt, result.data.from),
                            photos = result.data.photos,
                            showLoadingSpinner = false,
                            isInEditMode = isEditMode
                        )
                    }
                }

                is Result.Error -> {
                    println("failed to load event :(")
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

    private fun loadDataForTask(isEditMode: Boolean) {
        if (agendaItemId == null) {
            throw IllegalArgumentException("agendaItemId is null")
        }

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.loadTask(taskId = agendaItemId)

            when (result) {
                is Result.Success -> {
                    println("task loaded!")
                    _viewState.update {
                        it.copy(
                            title = result.data.title,
                            description = result.data.description,
                            fromTime = result.data.time.toHHmmString(),
                            fromDate = result.data.time.toMillisToMmmDdYyyy(),
                            reminderTime = getReminderTime(result.data.remindAt, result.data.time),
                            showLoadingSpinner = false,
                            isInEditMode = isEditMode
                        )
                    }

                }

                is Result.Error -> {
                    println("failed to load task :(")
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

    private fun loadDataForReminder(isEditMode: Boolean) {
        if (agendaItemId == null) {
            throw IllegalArgumentException("agendaItemId is null")
        }

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.loadReminder(reminderId = agendaItemId)

            when (result) {
                is Result.Success -> {
                    println("reminder loaded!")
                    _viewState.update {
                        it.copy(
                            title = result.data.title,
                            description = result.data.description,
                            fromTime = result.data.time.toHHmmString(),
                            fromDate = result.data.time.toMillisToMmmDdYyyy(),
                            reminderTime = getReminderTime(result.data.remindAt, result.data.time),
                            showLoadingSpinner = false,
                            isInEditMode = isEditMode
                        )
                    }

                }

                is Result.Error -> {
                    println("failed to load reminder :(")
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

    private fun getReminderTime(remindAt: Long, fromTime: Long): ReminderTime {
        val secondsBetween = DateTimeHelper.calculateTimeDifferenceInSeconds(remindAt, fromTime)
        val reminderTime = getReminderTimeForDuration(secondsBetween)
        return reminderTime
    }

    private fun getReminderTimeForDuration(durationInSeconds: Long): ReminderTime {
        return ReminderTime.entries.find { it.epochMilliSeconds == durationInSeconds }
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

    fun deleteAgendaItem() {
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
        val eventId = agendaItemId ?: throw IllegalArgumentException("agendaItemId is null")
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
        val taskId = agendaItemId ?: throw IllegalArgumentException("agendaItemId is null")
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
        val reminderId = agendaItemId ?: throw IllegalArgumentException("agendaItemId is null")
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

    private fun updateEvent() {
        val eventId = agendaItemId ?: throw IllegalArgumentException("agendaItemId is null")
        val fromInZoneDateTime =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val toInZoneDateTime =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.toDate,
                time = _viewState.value.toTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.updateEvent(
                eventDetails = EventDetailsUpdated(
                    id = eventId,
                    title = _viewState.value.title ?: "",
                    description = _viewState.value.description ?: "",
                    from = fromInZoneDateTime.toEpochMillis(),
                    to = toInZoneDateTime.toEpochMillis(),
                    remindAt = fromInZoneDateTime - reminderTime,
                    attendeeIds = listOf("a", "b"), //todo fix
                    deletedPhotoKeys = listOf(), //todo fix
                    isGoing = false //todo fix
                ),
                photos = _viewState.value.photos.filterIsInstance<Photo.LocalPhoto>()
            )

            when (result) {
                is Result.Success -> {
                    println("success update event!")
                    _viewEvent.send(AgendaDetailsViewEvent.NavigateToAgenda)
                    println("response = ${result.data}")
                }

                is Result.Error -> {
                    println("failed event update :(")
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

    private fun updateTask() {
        val taskId = agendaItemId ?: throw IllegalArgumentException("agendaItemId is null")
        val atInEpochSeconds =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.updateTask(
                taskDetails = AgendaItem.Task(
                    id = taskId,
                    title = _viewState.value.title ?: "",
                    description = _viewState.value.description ?: "",
                    time = atInEpochSeconds,
                    remindAt = atInEpochSeconds - reminderTime,
                    isDone = false //todo remove hardcode
                )
            )

            when (result) {
                is Result.Success -> {
                    println("success task updated!")
                    _viewEvent.send(AgendaDetailsViewEvent.NavigateToAgenda)
                }

                is Result.Error -> {
                    println("failed task updated :(")
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

    private fun updateReminder() {
        val reminderId = agendaItemId ?: throw IllegalArgumentException("agendaItemId is null")
        val atInEpochSeconds =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.updateReminder(
                reminderDetails = AgendaItem.Reminder(
                    id = reminderId,
                    title = _viewState.value.title ?: "",
                    description = _viewState.value.description ?: "",
                    time = atInEpochSeconds,
                    remindAt = atInEpochSeconds - reminderTime
                )
            )

            when (result) {
                is Result.Success -> {
                    println("success reminder updated!")
                    _viewEvent.send(AgendaDetailsViewEvent.NavigateToAgenda)
                }

                is Result.Error -> {
                    println("failed reminder updated :(")
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

    fun saveAgendaItem() {
        // todo save all items in local DB
        if (agendaItemType == null) {
            throw IllegalArgumentException("agendaItemType is null")
        }

        if (agendaItemId == null) {
            println("saved button clicked - create $agendaItemType")
            when (agendaItemType) {
                AgendaItemType.Event -> createEvent()
                AgendaItemType.Task -> createTask()
                AgendaItemType.Reminder -> createReminder()
            }
        } else {
            println("saved button clicked - update $agendaItemType")
            when (agendaItemType) {
                AgendaItemType.Event -> updateEvent()
                AgendaItemType.Task -> updateTask()
                AgendaItemType.Reminder -> updateReminder()
            }
        }
    }

    private fun createTask() {
        val atInEpochSeconds =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

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

    private fun createReminder() {
        val atInEpochSeconds =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

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

    private fun createEvent() {
        val fromInZoneDateTime =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val toInZoneDateTime =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.toDate,
                time = _viewState.value.toTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.createEvent(
                eventDetails = EventDetails(
                    id = UUID.randomUUID().toString(),
                    title = _viewState.value.title ?: "",
                    description = _viewState.value.description ?: "",
                    from = fromInZoneDateTime.toEpochMillis(),
                    to = toInZoneDateTime.toEpochMillis(),
                    remindAt = fromInZoneDateTime.toEpochMillis() - reminderTime,
                    attendeeIds = listOf("a", "b")
                ),
                photos = _viewState.value.photos.filterIsInstance<Photo.LocalPhoto>()
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

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun toggleVisitorDialog() {
        _viewState.update {
            it.copy(
                isAddVisitorDialogVisible = !it.isAddVisitorDialogVisible
            )
        }
    }

    fun addVisitor() {
        viewModelScope.launch {
            println("add visitor ${_email.value}")
            val result = agendaDetailsRemoteRepository.getAttendee(_email.value)

            when (result) {
                is Result.Success -> {
                    println("get visitor info success!")
                    if (result.data.doesUserExist) {
                        println("add visitor success!")
                        val newVisitor = result.data.attendee!!
                        val attendeeWithInitials = newVisitor.copy(
                            userInitials = ProfileUtils.getInitials(newVisitor.fullName)
                        )
                        _viewState.update {
                            it.copy(
                                showVisitorDoesNotExist = false,
                                showLoadingSpinner = false,
                                isAddVisitorDialogVisible = false,
                                visitorGoingList = it.visitorGoingList + attendeeWithInitials
                            )
                        }
                        println("visitorGoingList = ${_viewState.value.visitorGoingList}")
                    } else {
                        println("visitor email does not exist")
                        _viewState.update {
                            it.copy(
                                showLoadingSpinner = false,
                                showVisitorDoesNotExist = true
                            )
                        }
                    }
                }

                is Result.Error -> {
                    println("failed to add visitor :(")
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

    fun removeVisitor(visitor: AttendeeBasicInfoDetails) {
        _viewState.update { currentState ->
            val updatedList = currentState.visitorGoingList.filterNot { it == visitor }
            currentState.copy(visitorGoingList = updatedList)
        }
    }

    fun deleteAttendee() {
        val eventId = agendaItemId ?: throw IllegalArgumentException("agendaItemId is null")
        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val result = agendaDetailsRemoteRepository.deleteAttendee(eventId = eventId)

            when (result) {
                is Result.Success -> {
                    println("attendee removed!")
                    _viewState.update {
                        it.copy(
                            showLoadingSpinner = false
                        )
                    }

                }

                is Result.Error -> {
                    println("failed to remove attendee :(")
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
}