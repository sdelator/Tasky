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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tasky.EditingNav
import com.example.tasky.PhotoDetailNav
import com.example.tasky.R
import com.example.tasky.agenda_details.presentation.components.AttendeeSection
import com.example.tasky.agenda_details.presentation.components.EmptyPhotos
import com.example.tasky.agenda_details.presentation.components.Photos
import com.example.tasky.common.domain.Constants
import com.example.tasky.common.domain.util.convertMillisToDate
import com.example.tasky.common.presentation.DateLineItem
import com.example.tasky.common.presentation.GrayDivider
import com.example.tasky.common.presentation.LineItemType
import com.example.tasky.common.presentation.ReminderSection
import com.example.tasky.common.presentation.ReminderTime
import com.example.tasky.common.presentation.RightCarrotIcon
import com.example.tasky.common.presentation.TitleSection
import com.example.tasky.common.presentation.editing.TextFieldType
import com.example.tasky.common.presentation.model.AgendaItemType
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
    val isEditing = true
    val maxPhotoCount = 10

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

    val maxPhotosSelection = maxOf(2, maxPhotoCount - viewState.photos.size)
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxPhotosSelection),
        onResult = { photoUris ->
            agendaDetailsViewModel.onAddPhotosClick(photoUris.map { it.toString() })
        }
    )

    AgendaDetailsContent(
        titleText = viewState.title ?: title ?: getDefaultTitle(agendaItemType),
        itemDescription = viewState.description ?: description ?: getDefaultDescription(
            agendaItemType
        ),
        toDate = viewState.toDate,
        fromDate = viewState.fromDate,
        isEditMode = isEditing,
        onEditClick = { navController.navigate(EditingNav(it.name, agendaItemType.name)) },
        dateOnToolbar = date.convertMillisToDate(),
        onToolbarAction = {},
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
            multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        photosUriList = agendaDetailsViewModel.getUrisOrUrlsFromPhotoList(viewState.photos)
            .map { it.let { uriString -> Uri.parse(uriString) } },
        photoSkipCount = viewState.photoSkipCount,
        onPhotoClick = { uri ->
            agendaDetailsViewModel.setSelectedImage(uri.toString())
        },
        resetPhotoSkipCount = { agendaDetailsViewModel.resetPhotoSkipCount() }
    )
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
    resetPhotoSkipCount: () -> Unit
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
            isEditing = true
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp, bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                HeaderSection(agendaItemType = agendaItemType)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                TitleSection(
                    title = titleText,
                    isEditMode = isEditMode,
                    onEditClick = onEditClick
                )
                GrayDivider()
                DescriptionSection(
                    description = itemDescription,
                    isEditMode = isEditMode,
                    onEditClick = onEditClick
                )
                if (agendaItemType == AgendaItemType.Event) {
                    if (photosUriList.isEmpty()) {
                        EmptyPhotos(onAddPhotosClick = onAddPhotosClick)
                    } else {
                        Photos(
                            photoUriList = photosUriList,
                            photoSkipCount = photoSkipCount,
                            onAddPhotosClick = onAddPhotosClick,
                            onPhotoClick = onPhotoClick,
                            resetPhotoSkipCount = resetPhotoSkipCount
                        )
                    }
                }
                GrayDivider()
                DateLineItem(
                    date = fromDate,
                    dialogState = fromDateDialogState,
                    timeDialogState = fromTimeDialogState,
                    onDateSelected = onDateSelected,
                    onTimeSelected = onTimeSelected,
                    time = startTime,
                    buttonType = LineItemType.FROM,
                    isEditing = isEditMode
                )
                GrayDivider()
                if (agendaItemType == AgendaItemType.Event) {
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
                GrayDivider()
                ReminderSection(
                    reminderTime = reminderTime,
                    showReminderDropdown = showReminderDropdown,
                    toggleReminderDropdownVisibility = toggleReminderDropdownVisibility,
                    onReminderClick = onReminderClick
                )
                GrayDivider()
                if (agendaItemType == AgendaItemType.Event) {
                    AttendeeSection(
                        attendeeFilter = attendeeFilter,
                        onAttendeeFilterClick = onAttendeeFilterClick
                    )
                }
                //DeleteButton()
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
        resetPhotoSkipCount = {}
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