package com.example.tasky.actions.presentation

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tasky.R
import com.example.tasky.actions.presentation.components.EmptyPhotos
import com.example.tasky.common.domain.util.convertMillisToDate
import com.example.tasky.common.presentation.DateLineItem
import com.example.tasky.common.presentation.GrayDivider
import com.example.tasky.common.presentation.LineItemType
import com.example.tasky.common.presentation.RightCarrotIcon
import com.example.tasky.common.presentation.TitleSection
import com.example.tasky.common.presentation.model.Action
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.LocalTime

@Composable
fun ActionRoot(
    navController: NavController,
    action: Action,
    date: Long,
    actionViewModel: ActionViewModel = hiltViewModel()
) {
    val viewState by actionViewModel.viewState.collectAsStateWithLifecycle()
    val isEditing = (viewState.eventViewMode == EventViewMode.EDITABLE)

    val onUpdateTimeSelected: (LocalTime, LineItemType, MaterialDialogState) -> Unit =
        { time, lineItemType, dialogState ->
            eventViewModel.onUpdateTimeSelected(time, lineItemType, dialogState)
    }

    val updateDateDialogState: (MaterialDialogState, LineItemType) -> Unit =
        { dialogState, timeType ->
            eventViewModel.updateDateDialogState(dialogState, timeType)
        }

    ActionContent(
        isEditMode = isEditing,
        dateOnToolbar = date.convertMillisToDate(),
        onToolbarAction = {},
        fromDialogState = viewState.fromDatePickerDialogState,
        toDialogState = viewState.toDatePickerDialogState,
        fromTimeDialogState = viewState.fromTimeDialogState,
        toTimeDialogState = viewState.toTimeDialogState,
        actionType = action,
        dialogState = viewState.datePickerDialogState,
        timeDialogState = viewState.timePickerDialogState,
        updateDateDialogState = { eventViewModel.updateDateDialogState(it) },
        updateTimeDialogState = { eventViewModel.updateTimeDialogState(it) },
        onUpdateTimeSelected = onUpdateTimeSelected,
        startTime = viewState.fromTime,
        endTime = viewState.toTime
    )
}

@Composable
fun ActionContent(
    isEditMode: Boolean,
    dateOnToolbar: String,
    onToolbarAction: (ToolbarAction) -> Unit,
    fromDialogState: MaterialDialogState,
    toDialogState: MaterialDialogState,
    fromTimeDialogState: MaterialDialogState,
    toTimeDialogState: MaterialDialogState,
    actionType: Action,
    dialogState: MaterialDialogState,
    timeDialogState: MaterialDialogState,
    updateDateDialogState: (MaterialDialogState) -> Unit,
    updateTimeDialogState: (MaterialDialogState) -> Unit,
    onUpdateTimeSelected: (LocalTime, LineItemType, MaterialDialogState) -> Unit,
    startTime: String,
    endTime: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        ActionToolbar(
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
                HeaderSection(action = actionType)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                TitleSection(isEditMode = isEditMode)
                GrayDivider()
                DescriptionSection(action = actionType, isEditMode = isEditMode)
                if (actionType == Action.Event) {
                    EmptyPhotos() // TODO if statement for added photos
                }
                GrayDivider()
                DateLineItem(
                    dialogState = fromDialogState,
                    timeDialogState = fromTimeDialogState,
                    updateDateDialogState = updateDateDialogState,
                    updateTimeDialogState = updateTimeDialogState,
                    onUpdateTimeSelected = onUpdateTimeSelected,
                    time = startTime,
                    buttonType = LineItemType.FROM,
                    isEditing = isEditMode
                )
                GrayDivider()
                if (actionType == Action.Event) {
                    DateLineItem(
                        dialogState = toDialogState,
                        timeDialogState = toTimeDialogState,
                        updateDateDialogState = updateDateDialogState,
                        updateTimeDialogState = updateTimeDialogState,
                        onUpdateTimeSelected = onUpdateTimeSelected,
                        time = endTime,
                        buttonType = LineItemType.TO,
                        isEditing = isEditMode
                    )
                }
                GrayDivider()
                // TODO create rest of UI elements
//                ReminderSection()
//                if((actionType == Action.Event) {
//                    AttendeeSection()
//                }
//                DeleteButton()
            }
        }
    }
}

@Composable
fun HeaderSection(action: Action) {
    val actionColor = when (action) {
        Action.Event -> colorResource(id = R.color.event_light_green)
        Action.Task -> colorResource(id = R.color.tasky_green)
        Action.Reminder -> colorResource(id = R.color.reminder_gray)
    }

    val actionText = when (action) {
        Action.Event -> stringResource(id = R.string.event)
        Action.Task -> stringResource(id = R.string.task)
        Action.Reminder -> stringResource(id = R.string.reminder)
    }

    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(shape = RoundedCornerShape(2.dp))
                .background(actionColor),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = actionText,
            color = colorResource(id = R.color.dark_gray)
        )
    }
}

@Composable
fun DescriptionSection(action: Action, isEditMode: Boolean) {
    val actionText = when (action) {
        Action.Event -> stringResource(id = R.string.event_description)
        Action.Task -> stringResource(id = R.string.task_description)
        Action.Reminder -> stringResource(id = R.string.reminder_description)
    }

    Row(
        modifier = Modifier.padding(start = 16.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = actionText,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isEditMode) {
            RightCarrotIcon()
        }
    }
}