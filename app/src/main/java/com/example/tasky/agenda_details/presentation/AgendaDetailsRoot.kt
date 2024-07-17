package com.example.tasky.agenda_details.presentation

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
import com.example.tasky.R
import com.example.tasky.agenda_details.presentation.components.EmptyPhotos
import com.example.tasky.common.domain.util.convertMillisToDate
import com.example.tasky.common.presentation.DateLineItem
import com.example.tasky.common.presentation.GrayDivider
import com.example.tasky.common.presentation.LineItemType
import com.example.tasky.common.presentation.RightCarrotIcon
import com.example.tasky.common.presentation.TitleSection
import com.example.tasky.common.presentation.editing.TextFieldType
import com.example.tasky.common.presentation.model.AgendaDetailsType
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AgendaDetailsRoot(
    navController: NavController,
    agendaDetailsType: AgendaDetailsType,
    date: Long,
    title: String?,
    agendaDetailsViewModel: AgendaDetailsViewModel = hiltViewModel()
) {
    val viewState by agendaDetailsViewModel.viewState.collectAsStateWithLifecycle()
    val isEditing = true

    val onTimeSelected: (LocalTime, LineItemType, MaterialDialogState) -> Unit =
        { time, lineItemType, dialogState ->
            agendaDetailsViewModel.onTimeSelected(time, lineItemType, dialogState)
        }

    val onDateSelected: (LocalDate, MaterialDialogState, LineItemType) -> Unit =
        { selectedDate, dialogState, timeType ->
            agendaDetailsViewModel.onDateSelected(selectedDate, dialogState, timeType)
        }

    val titleText = title ?: stringResource(R.string.new_event)


    AgendaDetailsContent(
        titleText = titleText,
        toDate = viewState.toDate,
        fromDate = viewState.fromDate,
        isEditMode = isEditing,
        onEditClick = { navController.navigate(EditingNav(it.name)) },
        dateOnToolbar = date.convertMillisToDate(),
        onToolbarAction = {},
        fromDateDialogState = viewState.fromDatePickerDialogState,
        toDateDialogState = viewState.toDatePickerDialogState,
        fromTimeDialogState = viewState.fromTimeDialogState,
        toTimeDialogState = viewState.toTimeDialogState,
        agendaDetailsType = agendaDetailsType,
        onDateSelected = onDateSelected,
        onTimeSelected = onTimeSelected,
        startTime = viewState.fromTime,
        endTime = viewState.toTime
    )
}

@Composable
fun AgendaDetailsContent(
    titleText: String,
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
    agendaDetailsType: AgendaDetailsType,
    onDateSelected: (LocalDate, MaterialDialogState, LineItemType) -> Unit,
    onTimeSelected: (LocalTime, LineItemType, MaterialDialogState) -> Unit,
    startTime: String,
    endTime: String
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
                HeaderSection(agendaDetailsType = agendaDetailsType)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                TitleSection(
                    title = titleText,
                    isEditMode = isEditMode,
                    onEditClick = onEditClick
                )
                GrayDivider()
                DescriptionSection(
                    agendaDetailsType = agendaDetailsType,
                    isEditMode = isEditMode,
                    onEditClick = onEditClick
                )
                if (agendaDetailsType == AgendaDetailsType.Event) {
                    EmptyPhotos() // TODO if statement for added photos
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
                if (agendaDetailsType == AgendaDetailsType.Event) {
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
                // TODO create rest of UI elements
//                ReminderSection()
//                if((agendaDetailsType == Action.Event) {
//                    AttendeeSection()
//                }
//                DeleteButton()
            }
        }
    }
}

@Composable
fun HeaderSection(agendaDetailsType: AgendaDetailsType) {
    val agendaDetailsTypeColor = when (agendaDetailsType) {
        AgendaDetailsType.Event -> colorResource(id = R.color.event_light_green)
        AgendaDetailsType.Task -> colorResource(id = R.color.tasky_green)
        AgendaDetailsType.Reminder -> colorResource(id = R.color.reminder_gray)
    }

    val agendaDetailsTypeText = when (agendaDetailsType) {
        AgendaDetailsType.Event -> stringResource(id = R.string.event)
        AgendaDetailsType.Task -> stringResource(id = R.string.task)
        AgendaDetailsType.Reminder -> stringResource(id = R.string.reminder)
    }

    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(shape = RoundedCornerShape(2.dp))
                .background(agendaDetailsTypeColor),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = agendaDetailsTypeText,
            color = colorResource(id = R.color.dark_gray)
        )
    }
}

@Composable
fun DescriptionSection(
    agendaDetailsType: AgendaDetailsType,
    isEditMode: Boolean,
    onEditClick: (TextFieldType) -> Unit
) {
    val agendaDetailsTypeText = when (agendaDetailsType) {
        AgendaDetailsType.Event -> stringResource(id = R.string.event_description)
        AgendaDetailsType.Task -> stringResource(id = R.string.task_description)
        AgendaDetailsType.Reminder -> stringResource(id = R.string.reminder_description)
    }

    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 24.dp)
            .clickable { onEditClick(TextFieldType.DESCRIPTION) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = agendaDetailsTypeText,
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
        agendaDetailsType = AgendaDetailsType.Event
    )
}