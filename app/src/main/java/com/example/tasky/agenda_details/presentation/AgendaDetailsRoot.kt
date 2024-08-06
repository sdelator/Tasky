package com.example.tasky.agenda_details.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tasky.AgendaNav
import com.example.tasky.CalendarNavRoute
import com.example.tasky.EditingNav
import com.example.tasky.PhotoDetailNav
import com.example.tasky.R
import com.example.tasky.agenda_details.domain.model.AttendeeBasicInfoDetails
import com.example.tasky.agenda_details.presentation.components.AttendeeSection
import com.example.tasky.agenda_details.presentation.components.EmptyPhotos
import com.example.tasky.agenda_details.presentation.components.Photos
import com.example.tasky.common.domain.Constants
import com.example.tasky.common.domain.model.AgendaItemType
import com.example.tasky.common.domain.util.convertMillisToDateDdMmmYyyy
import com.example.tasky.common.presentation.CreateErrorAlertDialog
import com.example.tasky.common.presentation.DateLineItem
import com.example.tasky.common.presentation.GrayDivider
import com.example.tasky.common.presentation.LineItemType
import com.example.tasky.common.presentation.LoadingSpinner
import com.example.tasky.common.presentation.ObserveAsEvents
import com.example.tasky.common.presentation.ReminderSection
import com.example.tasky.common.presentation.ReminderTime
import com.example.tasky.common.presentation.RightCarrotIcon
import com.example.tasky.common.presentation.TitleSection
import com.example.tasky.common.presentation.editing.TextFieldType
import com.example.tasky.feature_agenda.presentation.toLogOutErrorMessage
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AgendaDetailsRoot(
    navController: NavController,
    agendaItemType: AgendaItemType,
    date: Long,
    title: String?,
    description: String?,
    imageAction: String?,
    agendaDetailsViewModel: AgendaDetailsViewModel = hiltViewModel()
) {
    val viewState by agendaDetailsViewModel.viewState.collectAsStateWithLifecycle()
    val email by agendaDetailsViewModel.email.collectAsStateWithLifecycle()
    val isEmailValid by agendaDetailsViewModel.isEmailValid.collectAsStateWithLifecycle()
    val maxPhotoCount = 10

    LaunchedEffect(navController.currentBackStackEntry) {
        agendaDetailsViewModel.restoreViewState(title, description)
    }

    // handle Cancel or Delete clicked from PhotoDetails screen
    LaunchedEffect(imageAction) {
        val imageDetailsAction =
            enumValueOf<PhotoDetailAction>(imageAction ?: PhotoDetailAction.NONE.name)
        agendaDetailsViewModel.handlePhotoDetailAction(imageDetailsAction)
        navController.currentBackStackEntry?.savedStateHandle?.set(
            Constants.IMAGE_ACTION,
            PhotoDetailAction.NONE.name
        )
    }

    LaunchedEffect(viewState.photoUri) {
        if (viewState.photoUri != null && imageAction == PhotoDetailAction.NONE.name) {
            navController.navigate(PhotoDetailNav(viewState.photoUri.toString()))
        }
    }

    val onTimeSelected: (LocalTime, LineItemType, MaterialDialogState) -> Unit =
        { time, lineItemType, dialogState ->
            agendaDetailsViewModel.onTimeSelected(time, lineItemType, dialogState)
        }

    val onDateSelected: (LocalDate, MaterialDialogState, LineItemType) -> Unit =
        { selectedDate, dialogState, timeType ->
            agendaDetailsViewModel.onDateSelected(selectedDate, dialogState, timeType)
        }

    val maxPhotosSelection = maxPhotoCount - viewState.photos.size
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = maxOf(
                2,
                maxPhotosSelection
            )
        ),
        onResult = { photoUris ->
            agendaDetailsViewModel.onAddPhotosClick(photoUris.map { it.toString() })
        }
    )
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { photoUri ->
            agendaDetailsViewModel.onAddPhotosClick(listOf(photoUri.toString()))
        }
    )

    AgendaDetailsContent(
        titleText = viewState.title ?: title ?: getDefaultTitle(agendaItemType),
        itemDescription = viewState.description ?: description ?: getDefaultDescription(
            agendaItemType
        ),
        toDate = viewState.toDate,
        fromDate = viewState.fromDate,
        isEditMode = viewState.isInEditMode,
        onEditClick = { navController.navigate(EditingNav(it.name, agendaItemType.name)) },
        dateOnToolbar = date.convertMillisToDateDdMmmYyyy(),
        onToolbarAction = {
            when (it) {
                ToolbarAction.SAVE -> agendaDetailsViewModel.saveAgendaItem()
                ToolbarAction.CANCEL -> navController.navigateUp()
                ToolbarAction.EDIT -> agendaDetailsViewModel.activateEditModeForAgendaItem()
            }
        },
        fromDateDialogState = viewState.fromDatePickerDialogState,
        toDateDialogState = viewState.toDatePickerDialogState,
        fromTimeDialogState = viewState.fromTimeDialogState,
        toTimeDialogState = viewState.toTimeDialogState,
        agendaItemType = agendaItemType,
        onDateSelected = onDateSelected,
        onTimeSelected = onTimeSelected,
        startTime = viewState.fromTime,
        endTime = viewState.toTime,
        showReminderDropdown = viewState.showReminderDropdown,
        toggleReminderDropdownVisibility = { agendaDetailsViewModel.toggleReminderDropdownVisibility() },
        onReminderClick = { agendaDetailsViewModel.setReminder(it) },
        reminderTime = viewState.reminderTime,
        attendeeFilter = viewState.attendeeFilterSelected,
        onAttendeeFilterClick = { agendaDetailsViewModel.setAttendeeFilter(it) },
        onAddPhotosClick = {
            if (maxPhotosSelection > 1) {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            } else {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        },
        photosUriList = agendaDetailsViewModel.getUrisOrUrlsFromPhotoList(viewState.photos)
            .map { it.let { uriString -> Uri.parse(uriString) } },
        photoSkipCount = viewState.photoSkipCount,
        onPhotoClick = { uri ->
            agendaDetailsViewModel.setSelectedImage(uri.toString())
        },
        resetPhotoSkipCount = { agendaDetailsViewModel.resetPhotoSkipCount() },
        onItemDelete = { agendaDetailsViewModel.deleteAgendaItem() },
        visitorEmail = email,
        isVisitorEmailValid = isEmailValid,
        onVisitorEmailChange = { agendaDetailsViewModel.onEmailChange(it) },//(String) -> Unit,
        onToggleVisitorDialog = { agendaDetailsViewModel.toggleVisitorDialog() },
        onAddVisitorClick = { agendaDetailsViewModel.addVisitor() },
        isAddVisitorDialogVisible = viewState.isAddVisitorDialogVisible,
        visitorList = viewState.visitorList,
        showVisitorDoesNotExist = viewState.showVisitorDoesNotExist
    )

    if (viewState.showLoadingSpinner) {
        LoadingSpinner()
    }

    if (viewState.showErrorDialog) {
        val message = viewState.dataError.toLogOutErrorMessage(context = LocalContext.current)
        CreateErrorAlertDialog(
            showDialog = true,
            dialogMessage = message,
            onDismiss = { agendaDetailsViewModel.onErrorDialogDismissed() }
        )
    }

    ObserveAsEvents(flow = agendaDetailsViewModel.viewEvent) { event ->
        when (event) {
            is AgendaDetailsViewEvent.NavigateToAgenda -> {
                navController.navigate(AgendaNav) {
                    popUpTo(CalendarNavRoute) {
                        inclusive = false
                    }
                }
            }
        }
    }
}

@Composable
fun AgendaDetailsContent(
    titleText: String,
    itemDescription: String,
    toDate: String,
    fromDate: String,
    isEditMode: Boolean,
    onEditClick: (TextFieldType) -> Unit,
    dateOnToolbar: String,
    onToolbarAction: (ToolbarAction) -> Unit,
    fromDateDialogState: MaterialDialogState,
    toDateDialogState: MaterialDialogState,
    fromTimeDialogState: MaterialDialogState,
    toTimeDialogState: MaterialDialogState,
    agendaItemType: AgendaItemType,
    onDateSelected: (LocalDate, MaterialDialogState, LineItemType) -> Unit,
    onTimeSelected: (LocalTime, LineItemType, MaterialDialogState) -> Unit,
    startTime: String,
    endTime: String,
    showReminderDropdown: Boolean,
    toggleReminderDropdownVisibility: () -> Unit,
    onReminderClick: (ReminderTime) -> Unit,
    reminderTime: ReminderTime,
    attendeeFilter: AttendeeFilter,
    onAttendeeFilterClick: (AttendeeFilter) -> Unit,
    onAddPhotosClick: () -> Unit,
    photosUriList: List<Uri?>,
    photoSkipCount: Int,
    onPhotoClick: (Uri) -> Unit,
    resetPhotoSkipCount: () -> Unit,
    onItemDelete: () -> Unit,
    visitorEmail: String,
    isVisitorEmailValid: Boolean,
    onVisitorEmailChange: (String) -> Unit,
    onToggleVisitorDialog: () -> Unit,
    onAddVisitorClick: () -> Unit,
    isAddVisitorDialogVisible: Boolean,
    visitorList: List<AttendeeBasicInfoDetails>,
    showVisitorDoesNotExist: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        AgendaDetailsToolbar(
            date = dateOnToolbar,
            onToolbarAction = onToolbarAction,
            isEditing = isEditMode
        )
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            shape = RoundedCornerShape(
                topStart = 30.dp, topEnd = 30.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp, bottom = 16.dp)
            ) {
                item { HeaderSection(agendaItemType = agendaItemType) }
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                item {
                    TitleSection(
                        title = titleText,
                        isEditMode = isEditMode,
                        onEditClick = onEditClick
                    )
                }
                item { GrayDivider() }
                item {
                    DescriptionSection(
                        description = itemDescription,
                        isEditMode = isEditMode,
                        onEditClick = onEditClick
                    )
                }
                if (agendaItemType == AgendaItemType.Event) {
                    if (photosUriList.isEmpty()) {
                        item { EmptyPhotos(onAddPhotosClick = onAddPhotosClick) }
                    }
                } else {
                    item {
                        Photos(
                            photoUriList = photosUriList,
                            photoSkipCount = photoSkipCount,
                            onAddPhotosClick = onAddPhotosClick,
                            onPhotoClick = onPhotoClick,
                            resetPhotoSkipCount = resetPhotoSkipCount
                        )
                    }
                }
                item { GrayDivider() }
                item {
                    DateLineItem(
                        date = fromDate,
                        dialogState = fromDateDialogState,
                        timeDialogState = fromTimeDialogState,
                        onDateSelected = onDateSelected,
                        onTimeSelected = onTimeSelected,
                        time = startTime,
                        buttonType = if (agendaItemType == AgendaItemType.Event) {
                            LineItemType.FROM
                        } else LineItemType.AT,
                        isEditing = isEditMode
                    )
                }
                if (agendaItemType == AgendaItemType.Event) {
                    item { GrayDivider() }
                    item {
                        DateLineItem(
                            date = toDate,
                            dialogState = toDateDialogState,
                            timeDialogState = toTimeDialogState,
                            onDateSelected = onDateSelected,
                            onTimeSelected = onTimeSelected,
                            time = endTime,
                            buttonType = LineItemType.TO,
                            isEditing = isEditMode
                        )
                    }
                }
                item { GrayDivider() }
                item {
                    ReminderSection(
                        reminderTime = reminderTime,
                        showReminderDropdown = showReminderDropdown,
                        toggleReminderDropdownVisibility = toggleReminderDropdownVisibility,
                        onReminderClick = onReminderClick
                    )
                }
                item { GrayDivider() }
                if (agendaItemType == AgendaItemType.Event) {
                    item {
                        AttendeeSection(
                            isEditMode = isEditMode,
                            attendeeFilter = attendeeFilter,
                            onAttendeeFilterClick = onAttendeeFilterClick,
                            visitorEmail = visitorEmail,
                            isVisitorEmailValid = isVisitorEmailValid,
                            onVisitorEmailChange = onVisitorEmailChange,
                            onToggleVisitorDialog = onToggleVisitorDialog,
                            onAddVisitorClick = onAddVisitorClick,
                            isAddVisitorDialogVisible = isAddVisitorDialogVisible,
                            visitorList = visitorList,
                            showVisitorDoesNotExist = showVisitorDoesNotExist
                        )
                    }
                }
                item { Spacer(modifier = Modifier.weight(1f)) }
                item { DeleteButton(agendaItemType, onItemDelete) }
            }
        }
    }
}

@Composable
fun HeaderSection(agendaItemType: AgendaItemType) {
    val agendaItemTypeColor = when (agendaItemType) {
        AgendaItemType.Event -> colorResource(id = R.color.event_light_green)
        AgendaItemType.Task -> colorResource(id = R.color.tasky_green)
        AgendaItemType.Reminder -> colorResource(id = R.color.reminder_gray)
    }

    val agendaItemTypeText = when (agendaItemType) {
        AgendaItemType.Event -> stringResource(id = R.string.event)
        AgendaItemType.Task -> stringResource(id = R.string.task)
        AgendaItemType.Reminder -> stringResource(id = R.string.reminder)
    }

    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(shape = RoundedCornerShape(2.dp))
                .background(agendaItemTypeColor),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = agendaItemTypeText,
            color = colorResource(id = R.color.dark_gray)
        )
    }
}

@Composable
fun DescriptionSection(
    description: String,
    isEditMode: Boolean,
    onEditClick: (TextFieldType) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 24.dp)
            .clickable { onEditClick(TextFieldType.DESCRIPTION) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = description,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isEditMode) {
            RightCarrotIcon()
        }
    }
}

@Composable
@Preview
fun PreviewEventContent() {
    AgendaDetailsContent(
        titleText = "Meeting",
        itemDescription = "test decription",
        toDate = "Jul 12 2024",
        fromDate = "Jul 11 2024",
        isEditMode = true,
        onEditClick = { },
        dateOnToolbar = "15 Jul 2024",
        onToolbarAction = {},
        fromDateDialogState = MaterialDialogState(),
        toDateDialogState = MaterialDialogState(),
        fromTimeDialogState = MaterialDialogState(),
        toTimeDialogState = MaterialDialogState(),
        onDateSelected = { _, _, _ -> },
        onTimeSelected = { _, _, _ -> },
        startTime = "08:00",
        endTime = "08:15",
        agendaItemType = AgendaItemType.Event,
        showReminderDropdown = false,
        toggleReminderDropdownVisibility = {},
        onReminderClick = {},
        reminderTime = ReminderTime.THIRTY_MINUTES,
        attendeeFilter = AttendeeFilter.GOING,
        onAttendeeFilterClick = {},
        onAddPhotosClick = {},
        photosUriList = emptyList(),
        photoSkipCount = 0,
        onPhotoClick = {},
        resetPhotoSkipCount = {},
        onItemDelete = {},
        visitorEmail = "",
        isVisitorEmailValid = true,
        onVisitorEmailChange = { _ -> },
        onToggleVisitorDialog = {},
        onAddVisitorClick = {},
        isAddVisitorDialogVisible = true,
        visitorList = listOf(),
        showVisitorDoesNotExist = false
    )
}

@Composable
fun getDefaultDescription(agendaItemType: AgendaItemType): String {
    return when (agendaItemType) {
        AgendaItemType.Event -> stringResource(id = R.string.event_description)
        AgendaItemType.Task -> stringResource(id = R.string.task_description)
        AgendaItemType.Reminder -> stringResource(id = R.string.reminder_description)
    }
}

@Composable
fun getDefaultTitle(agendaItemType: AgendaItemType): String {
    return when (agendaItemType) {
        AgendaItemType.Event -> stringResource(id = R.string.new_event)
        AgendaItemType.Task -> stringResource(id = R.string.new_task)
        AgendaItemType.Reminder -> stringResource(id = R.string.new_reminder)
    }
}

@Composable
fun DeleteButton(agendaItemType: AgendaItemType, onItemDelete: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onItemDelete() }
            .padding(bottom = 50.dp, top = 50.dp)
    ) {
        GrayDivider()
        Text(
            text = stringResource(R.string.delete, agendaItemType.name.uppercase()),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.gray)
        )
    }
}