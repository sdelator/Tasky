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
import com.example.tasky.common.domain.notification.NotificationHandler
import com.example.tasky.common.domain.util.EmailPatternValidatorImpl
import com.example.tasky.common.domain.util.convertMillisToZonedDateTime
import com.example.tasky.common.domain.util.toEpochMillis
import com.example.tasky.common.domain.util.toHHmmString
import com.example.tasky.common.domain.util.toMillisToMmmDdYyyy
import com.example.tasky.common.presentation.CardAction
import com.example.tasky.common.presentation.LineItemType
import com.example.tasky.common.presentation.ReminderTime
import com.example.tasky.common.presentation.util.ProfileUtils
import com.example.tasky.common.presentation.util.toFormatted_HH_mm
import com.example.tasky.common.presentation.util.toFormatted_MMM_dd_yyyy
import com.example.tasky.feature_agenda.data.local.EventEntity
import com.example.tasky.feature_agenda.data.local.ReminderEntity
import com.example.tasky.feature_agenda.data.local.TaskEntity
import com.example.tasky.feature_agenda.domain.local.EventLocalRepository
import com.example.tasky.feature_agenda.domain.local.ReminderLocalRepository
import com.example.tasky.feature_agenda.domain.local.TaskLocalRepository
import com.vanpra.composematerialdialogs.MaterialDialogState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AgendaDetailsViewModel @Inject constructor(
    private val emailPatternValidator: EmailPatternValidatorImpl,
    private val imageCompressor: ImageCompressor,
    private val agendaDetailsRemoteRepository: AgendaDetailsRemoteRepository,
    private val sessionStateManager: SessionStateManager,
    private val savedStateHandle: SavedStateHandle,
    private val notificationHandler: NotificationHandler,
    private val eventLocalRepository: EventLocalRepository,
    private val taskLocalRepository: TaskLocalRepository,
    private val reminderLocalRepository: ReminderLocalRepository
) : ViewModel() {

    companion object {
        const val TAG = "AgendaDetailsViewModel"
        const val DEFAULT_TIME_RANGE = 15L
    }

    private val selectedDate: Long? = savedStateHandle.get<Long>("date")
    private val agendaItemId: String? = savedStateHandle.get<String>("agendaItemId")
    private val agendaItemAction: CardAction? = savedStateHandle.get<String>("cardAction")?.let {
        CardAction.valueOf(it)
    }
    private val agendaItemType = savedStateHandle.get<String>("agendaItemType")?.let {
        AgendaItemType.valueOf(it)
    }

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
                    val zonedDateTime = selectedDate?.convertMillisToZonedDateTime()

                    val defaultDate = LocalDate.now().toFormatted_MMM_dd_yyyy()
                    val defaultTime = LocalTime.now().toFormatted_HH_mm()
                    val defaultToTime =
                        LocalTime.now().plusMinutes(DEFAULT_TIME_RANGE).toFormatted_HH_mm()

                    _viewState.update {
                        it.copy(
                            fromDate = zonedDateTime?.toMillisToMmmDdYyyy() ?: defaultDate,
                            toDate = zonedDateTime?.toMillisToMmmDdYyyy() ?: defaultDate,
                            fromTime = zonedDateTime?.toHHmmString() ?: defaultTime,
                            toTime = zonedDateTime?.plusMinutes(DEFAULT_TIME_RANGE)?.toHHmmString()
                                ?: defaultToTime,
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

    private fun getReminderTime(remindAt: ZonedDateTime, fromTime: ZonedDateTime): ReminderTime {
        val milliSecondsBetween = Duration.between(fromTime, remindAt).toMillis()
        val reminderTime = getReminderTimeForDuration(milliSecondsBetween)
        return reminderTime
    }

    private fun getReminderTimeForDuration(durationInMilliSeconds: Long): ReminderTime {
        return ReminderTime.entries.find { it.epochMilliSeconds == durationInMilliSeconds }
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
                    notificationHandler.cancelAlarmAndNotification(notificationId = eventId)
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
                    notificationHandler.cancelAlarmAndNotification(notificationId = taskId)
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
                    notificationHandler.cancelAlarmAndNotification(notificationId = reminderId)
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
            val title = _viewState.value.title ?: ""
            val description = _viewState.value.description ?: ""
            val remindAtInZDT =
                DateTimeHelper.calculateRemindAtZDT(fromInZoneDateTime, reminderTime)
            val remindAtInMillis = remindAtInZDT.toInstant().toEpochMilli()

            val result = agendaDetailsRemoteRepository.updateEvent(
                eventDetails = EventDetailsUpdated(
                    id = eventId,
                    title = title,
                    description = description,
                    from = fromInZoneDateTime.toEpochMillis(),
                    to = toInZoneDateTime.toEpochMillis(),
                    remindAt = remindAtInMillis,
                    attendeeIds = listOf("a", "b"), //todo fix
                    deletedPhotoKeys = listOf(), //todo fix
                    isGoing = false //todo fix
                ),
                photos = _viewState.value.photos.filterIsInstance<Photo.LocalPhoto>()
            )

            when (result) {
                is Result.Success -> {
                    println("success update event!")
                    // schedule notification for updated item only if reminder time is in the future
                    if (isReminderTimeInFuture(remindAtInMillis)) {
                        notificationHandler.updateNotification(
                            notificationId = eventId,
                            title = title,
                            description = description,
                            remindAt = remindAtInMillis
                        )
                    }

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

            withContext(Dispatchers.IO) {
                // update into local db
                eventLocalRepository.updateEvent(
                    event =
                    EventEntity(
                        id = eventId,
                        title = title,
                        description = description,
                        from = fromInZoneDateTime.toEpochMillis(),
                        to = toInZoneDateTime.toEpochMillis(),
                        remindAt = remindAtInMillis,
                        host = "", //todo fix hardcoding
                        isUserEventCreator = false //todo fix hardcoding
                    )
                )
            }
        }
    }

    private fun updateTask() {
        val taskId = agendaItemId ?: throw IllegalArgumentException("agendaItemId is null")
        val atInZoneDateTime =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val title = _viewState.value.title ?: ""
            val description = _viewState.value.description ?: ""
            val remindAtInZDT = DateTimeHelper.calculateRemindAtZDT(atInZoneDateTime, reminderTime)
            val remindAtInMillis = remindAtInZDT.toInstant().toEpochMilli()

            val result = agendaDetailsRemoteRepository.updateTask(
                taskDetails = AgendaItem.Task(
                    id = taskId,
                    title = title,
                    description = description,
                    time = atInZoneDateTime,
                    remindAt = remindAtInZDT,
                    isDone = false //todo remove hardcode
                )
            )

            when (result) {
                is Result.Success -> {
                    println("success task updated!")
                    // schedule notification for updated item only if reminder time is in the future
                    if (isReminderTimeInFuture(remindAtInMillis)) {
                        notificationHandler.updateNotification(
                            notificationId = taskId,
                            title = title,
                            description = description,
                            remindAt = remindAtInMillis
                        )
                    }
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

            withContext(Dispatchers.IO) {
                // update into local db
                taskLocalRepository.updateTask(
                    task =
                    TaskEntity(
                        id = taskId,
                        title = title,
                        description = description,
                        time = atInZoneDateTime.toEpochMillis(),
                        remindAt = remindAtInZDT.toEpochMillis(),
                        isDone = false //todo remove hardcode
                    )
                )
            }
        }
    }

    private fun updateReminder() {
        val reminderId = agendaItemId ?: throw IllegalArgumentException("agendaItemId is null")
        val atInZoneDateTime =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val title = _viewState.value.title ?: ""
            val description = _viewState.value.description ?: ""
            val remindAtInZDT = DateTimeHelper.calculateRemindAtZDT(atInZoneDateTime, reminderTime)
            val remindAtInMillis = remindAtInZDT.toInstant().toEpochMilli()

            val result = agendaDetailsRemoteRepository.updateReminder(
                reminderDetails = AgendaItem.Reminder(
                    id = reminderId,
                    title = title,
                    description = description,
                    time = atInZoneDateTime,
                    remindAt = remindAtInZDT
                )
            )

            when (result) {
                is Result.Success -> {
                    println("success reminder updated!")
                    // schedule notification for updated item only if reminder time is in the future
                    if (isReminderTimeInFuture(remindAtInMillis)) {
                        notificationHandler.updateNotification(
                            notificationId = reminderId,
                            title = title,
                            description = description,
                            remindAt = remindAtInMillis
                        )
                    }
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

            withContext(Dispatchers.IO) {
                // update into local db
                reminderLocalRepository.updateReminder(
                    reminder =
                    ReminderEntity(
                        id = reminderId,
                        title = title,
                        description = description,
                        time = atInZoneDateTime.toEpochMillis(),
                        remindAt = remindAtInZDT.toEpochMillis()
                    )
                )
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
        val atInZoneDateTime =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val taskId = UUID.randomUUID().toString()
            val title = _viewState.value.title ?: ""
            val description = _viewState.value.description ?: ""
            val remindAtInZDT = DateTimeHelper.calculateRemindAtZDT(atInZoneDateTime, reminderTime)
            val remindAtInMillis = remindAtInZDT.toInstant().toEpochMilli()

            val result = agendaDetailsRemoteRepository.createTask(
                taskDetails = AgendaItem.Task(
                    id = taskId,
                    title = title,
                    description = description,
                    time = atInZoneDateTime,
                    remindAt = remindAtInZDT,
                    isDone = false //todo remove hardcode
                )
            )

            when (result) {
                is Result.Success -> {
                    println("success task creation!")
                    // schedule notification for newly created only if reminder time is in the future
                    if (isReminderTimeInFuture(remindAtInMillis)) {
                        notificationHandler.initNotification(
                            notificationId = taskId,
                            title = title,
                            description = description,
                            remindAt = remindAtInMillis
                        )
                    }
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
            withContext(Dispatchers.IO) {
                // insert into local db
                taskLocalRepository.insertTask(
                    task =
                    TaskEntity(
                        id = taskId,
                        title = title,
                        description = description,
                        time = atInZoneDateTime.toEpochMillis(),
                        remindAt = remindAtInZDT.toEpochMillis(),
                        isDone = false //todo remove hardcode
                    )
                )
            }
        }
    }

    private fun isReminderTimeInFuture(reminderEpochMillis: Long): Boolean {
        val currentEpochMillis = System.currentTimeMillis()
        return reminderEpochMillis > currentEpochMillis
    }

    private fun createReminder() {
        val atInZoneDateTime =
            DateTimeHelper.getZonedDateTimeFromDateAndTime(
                date = _viewState.value.fromDate,
                time = _viewState.value.fromTime
            )
        val reminderTime = _viewState.value.reminderTime.epochMilliSeconds

        viewModelScope.launch {
            _viewState.update { it.copy(showLoadingSpinner = true) }
            val reminderId = UUID.randomUUID().toString()
            val title = _viewState.value.title ?: ""
            val description = _viewState.value.description ?: ""
            val remindAtInZDT = DateTimeHelper.calculateRemindAtZDT(atInZoneDateTime, reminderTime)
            val remindAtInMillis = remindAtInZDT.toInstant().toEpochMilli()

            val result = agendaDetailsRemoteRepository.createReminder(
                reminderDetails = AgendaItem.Reminder(
                    id = reminderId,
                    title = title,
                    description = description,
                    time = atInZoneDateTime,
                    remindAt = remindAtInZDT
                )
            )

            when (result) {
                is Result.Success -> {
                    println("success reminder creation!")
                    // schedule notification for newly created only if reminder time is in the future
                    if (isReminderTimeInFuture(remindAtInMillis)) {
                        notificationHandler.initNotification(
                            notificationId = reminderId,
                            title = title,
                            description = description,
                            remindAt = remindAtInMillis
                        )
                    }
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

            withContext(Dispatchers.IO) {
                // insert into local db
                reminderLocalRepository.insertReminder(
                    reminder =
                    ReminderEntity(
                        id = reminderId,
                        title = title,
                        description = description,
                        time = atInZoneDateTime.toEpochMillis(),
                        remindAt = remindAtInZDT.toEpochMillis()
                    )
                )
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
            val eventId = UUID.randomUUID().toString()
            val title = _viewState.value.title ?: ""
            val description = _viewState.value.description ?: ""
            val remindAtInZDT =
                DateTimeHelper.calculateRemindAtZDT(fromInZoneDateTime, reminderTime)
            val remindAtInMillis = remindAtInZDT.toInstant().toEpochMilli()

            val result = agendaDetailsRemoteRepository.createEvent(
                eventDetails = EventDetails(
                    id = eventId,
                    title = title,
                    description = description,
                    from = fromInZoneDateTime.toEpochMillis(),
                    to = toInZoneDateTime.toEpochMillis(),
                    remindAt = remindAtInMillis,
                    attendeeIds = listOf("a", "b")
                ),
                photos = _viewState.value.photos.filterIsInstance<Photo.LocalPhoto>()
            )

            when (result) {
                is Result.Success -> {
                    println("success event creation!")
                    // schedule notification for newly created only if reminder time is in the future
                    if (isReminderTimeInFuture(remindAtInMillis)) {
                        notificationHandler.initNotification(
                            notificationId = eventId,
                            title = title,
                            description = description,
                            remindAt = remindAtInMillis
                        )
                    }
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

            withContext(Dispatchers.IO) {
                // create into local db
                eventLocalRepository.insertEvent(
                    event =
                    EventEntity(
                        id = eventId,
                        title = title,
                        description = description,
                        from = fromInZoneDateTime.toEpochMillis(),
                        to = toInZoneDateTime.toEpochMillis(),
                        remindAt = remindAtInMillis,
                        host = "", //todo fix hardcoding
                        isUserEventCreator = true
                    )
                )
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
        val valid = emailPatternValidator.isValidEmailPattern(email)
        _viewState.update {
            it.copy(email = email, isEmailValid = valid)
        }
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
            println("add visitor ${_viewState.value.email}")
            val result = agendaDetailsRemoteRepository.getAttendee(_viewState.value.email)

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