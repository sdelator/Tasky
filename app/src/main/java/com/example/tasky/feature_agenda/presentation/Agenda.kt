package com.example.tasky.feature_agenda.presentation

import HorizontalCalendar
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tasky.AuthNavRoute
import com.example.tasky.EventNav
import com.example.tasky.R
import com.example.tasky.ReminderNav
import com.example.tasky.TaskNav
import com.example.tasky.agenda_details.domain.model.AgendaItem
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.domain.model.AgendaItemType
import com.example.tasky.common.presentation.AgendaCard
import com.example.tasky.common.presentation.CardAction
import com.example.tasky.common.presentation.CreateErrorAlertDialog
import com.example.tasky.common.presentation.FabDropdownRoot
import com.example.tasky.common.presentation.HeaderSmallBold
import com.example.tasky.common.presentation.LoadingSpinner
import com.example.tasky.common.presentation.ObserveAsEvents
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.ZonedDateTime

@Composable
fun AgendaRoot(
    navController: NavController,
    refreshData: Boolean,
    agendaViewModel: AgendaViewModel = hiltViewModel()
) {
    val viewState by agendaViewModel.viewState.collectAsStateWithLifecycle()
    val initials = agendaViewModel.initials

    val updateDateSelected: (Int, Int, Int?) -> Unit = { month, day, year ->
        agendaViewModel.updateDateSelected(month, day, year)
    }

    val formatTimeBasedOnEvent: (ZonedDateTime, ZonedDateTime?) -> String = { fromDate, toDate ->
        agendaViewModel.formatTimeOnAgendaCard(fromDate, toDate)
    }

    LaunchedEffect(refreshData) {
        if (refreshData) {
            agendaViewModel.refreshData()
        }
    }

    val onFabAgendaItemTypeClick: (AgendaItemType) -> Unit = {
        when (it) {
            AgendaItemType.Event -> {
                navController.navigate(
                    EventNav(
                        date = viewState.dateSelected,
                        agendaItemType = AgendaItemType.Event.name
                    )
                )
            }

            AgendaItemType.Task -> {
                navController.navigate(
                    TaskNav(
                        date = viewState.dateSelected,
                        agendaItemType = AgendaItemType.Task.name
                    )
                )
            }

            AgendaItemType.Reminder -> {
                navController.navigate(
                    ReminderNav(
                        date = viewState.dateSelected,
                        agendaItemType = AgendaItemType.Reminder.name
                    )
                )
            }
        }
    }

    val onAgendaCardActionClick: (AgendaItem, CardAction) -> Unit = { agendaItem, cardAction ->
        when (agendaItem.cardType) {
            AgendaItemType.Event -> {
                navController.navigate(
                    EventNav(
                        date = viewState.dateSelected,
                        agendaItemId = agendaItem.id,
                        cardAction = cardAction.name,
                        agendaItemType = agendaItem.cardType.name
                    )
                )
            }

            AgendaItemType.Task -> {
                navController.navigate(
                    TaskNav(
                        date = viewState.dateSelected,
                        agendaItemId = agendaItem.id,
                        cardAction = cardAction.name,
                        agendaItemType = agendaItem.cardType.name
                    )
                )
            }

            AgendaItemType.Reminder -> {
                navController.navigate(
                    ReminderNav(
                        date = viewState.dateSelected,
                        agendaItemId = agendaItem.id,
                        cardAction = cardAction.name,
                        agendaItemType = agendaItem.cardType.name
                    )
                )
            }
        }
    }

    AgendaContent(
        calendarDays = viewState.calendarDays,
        monthSelected = viewState.monthSelected,
        daySelected = viewState.daySelected,
        initials = initials,
        updateDateSelected = updateDateSelected,
        toggleLogoutDropdownVisibility = { agendaViewModel.toggleLogoutDropdownVisibility() },
        toggleFabDropdownVisibility = { agendaViewModel.toggleFabDropdownVisibility() },
        showLogoutDropdown = viewState.showLogoutDropdown,
        showFabDropdown = viewState.showFabDropdown,
        dialogState = viewState.datePickerDialogState,
        updateDateDialogState = { agendaViewModel.updateDateDialogState(it) },
        onLogoutClick = { agendaViewModel.logOutClicked() },
        onFabActionClick = onFabAgendaItemTypeClick,
        headerDateText = viewState.headerDateText,
        agendaItems = viewState.agendaItems,
        needlePosition = viewState.needlePosition,
        formatTimeBasedOnEvent = formatTimeBasedOnEvent,
        onAgendaCardActionClick = onAgendaCardActionClick,
        toggleAgendaCardDropdownVisibility = { agendaViewModel.toggleAgendaDropdownVisibility() },
        showAgendaCardDropdown = viewState.showAgendaCardDropdown,
        visibleAgendaCardDropdownId = viewState.visibleAgendaCardDropdownId,
        setVisibleAgendaItemId = { agendaViewModel.setVisibleAgendaItemId(it) },
        toggleTaskComplete = { agendaViewModel.toggleTaskComplete() },
        isTaskChecked = viewState.isTaskChecked,
        setTaskCompleteForAgendaItemId = { agendaViewModel.setTaskCompleteForAgendaItemId(it) },
        taskCompleteForAgendaItemId = viewState.taskCompleteForAgendaItemId
    )

    if (viewState.showLoadingSpinner) {
        LoadingSpinner()
    }

    if (viewState.showErrorDialog) {
        val message = viewState.dataError.toLogOutErrorMessage(context = LocalContext.current)
        CreateErrorAlertDialog(
            showDialog = true,
            dialogMessage = message,
            onDismiss = { agendaViewModel.onErrorDialogDismissed() }
        )
    }

    ObserveAsEvents(flow = agendaViewModel.viewEvent) { event ->
        when (event) {
            is AgendaViewEvent.NavigateToLoginScreen -> {
                navController.navigate(AuthNavRoute)
            }
        }
    }
}


@Composable
fun AgendaContent(
    calendarDays: List<CalendarDay>,
    monthSelected: Int,
    daySelected: Int,
    initials: String,
    updateDateSelected: (Int, Int, Int?) -> Unit,
    toggleLogoutDropdownVisibility: () -> Unit,
    toggleFabDropdownVisibility: () -> Unit,
    showLogoutDropdown: Boolean,
    showFabDropdown: Boolean,
    dialogState: MaterialDialogState,
    updateDateDialogState: (MaterialDialogState) -> Unit,
    onLogoutClick: () -> Unit,
    onFabActionClick: (AgendaItemType) -> Unit,
    headerDateText: String,
    agendaItems: List<AgendaItem>?,
    needlePosition: ZonedDateTime,
    formatTimeBasedOnEvent: (ZonedDateTime, ZonedDateTime?) -> String,
    showAgendaCardDropdown: Boolean,
    toggleAgendaCardDropdownVisibility: () -> Unit,
    onAgendaCardActionClick: (AgendaItem, CardAction) -> Unit,
    visibleAgendaCardDropdownId: String?,
    setVisibleAgendaItemId: (String) -> Unit,
    toggleTaskComplete: () -> Unit,
    isTaskChecked: Boolean,
    setTaskCompleteForAgendaItemId: (String) -> Unit,
    taskCompleteForAgendaItemId: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        AgendaToolbar(
            monthSelected = monthSelected,
            initials = initials,
            updateDateSelected = updateDateSelected,
            toggleLogoutDropdownVisibility = toggleLogoutDropdownVisibility,
            showLogoutDropdown = showLogoutDropdown,
            dialogState = dialogState,
            onDialogStateChange = updateDateDialogState,
            onLogoutClick = onLogoutClick
        )

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 75.dp),
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalCalendar(
                    calendarDays = calendarDays,
                    month = monthSelected,
                    day = daySelected,
                    updateDateSelected = { month, day ->
                        updateDateSelected(month, day, null)
                    }
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                HeaderSmallBold(title = headerDateText, modifier = Modifier.align(Alignment.Start))
                Spacer(modifier = Modifier.padding(top = 10.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!agendaItems.isNullOrEmpty()) {
                        itemsIndexed(
                            items = agendaItems
                        ) { index, agendaItem ->
                            if (index > 0 && agendaItem.startTime.toLocalDate() == needlePosition.toLocalDate() && agendaItem.startTime.isAfter(
                                    needlePosition
                                ) && agendaItems[index - 1].startTime.isBefore(needlePosition)
                            ) {
                                Needle()
                            }
                            AgendaCard(
                                agendaItem = agendaItem,
                                modifier = Modifier.animateItem(),
                                date = when (agendaItem) {
                                    is AgendaItem.Event -> formatTimeBasedOnEvent(
                                        agendaItem.from,
                                        agendaItem.to
                                    )

                                    is AgendaItem.Task, is AgendaItem.Reminder ->
                                        formatTimeBasedOnEvent(
                                            agendaItem.startTime,
                                            null
                                        )
                                },
                                isTaskChecked = isTaskChecked,
                                toggleAgendaCardDropdownVisibility = toggleAgendaCardDropdownVisibility,
                                onAgendaCardActionClick = onAgendaCardActionClick,
                                visibleAgendaCardDropdownId = visibleAgendaCardDropdownId,
                                setVisibleAgendaItemId = setVisibleAgendaItemId,
                                showAgendaCardDropdown = showAgendaCardDropdown,
                                toggleTaskComplete = toggleTaskComplete,
                                setTaskCompleteForAgendaItemId = setTaskCompleteForAgendaItemId,
                                taskCompleteForAgendaItemId = taskCompleteForAgendaItemId
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
                .zIndex(1f)
        ) {
            Fab(onClick = toggleFabDropdownVisibility)
            FabDropdownRoot(
                showFabDropdown = showFabDropdown,
                toggleFabDropdownVisibility = toggleFabDropdownVisibility,
                onFabActionClick = onFabActionClick,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
@Preview
fun PreviewAgendaContent() {
    AgendaContent(
        calendarDays = listOf(),
        monthSelected = 3,
        daySelected = 9,
        initials = "AB",
        updateDateSelected = { month, day, year -> },
        toggleLogoutDropdownVisibility = { },
        toggleFabDropdownVisibility = { },
        showLogoutDropdown = true,
        showFabDropdown = true,
        dialogState = MaterialDialogState(),
        updateDateDialogState = { },
        onLogoutClick = { },
        headerDateText = "today",
        onFabActionClick = { },
        agendaItems = null,
        formatTimeBasedOnEvent = { _, _ -> "" },
        onAgendaCardActionClick = { _, _ -> },
        showAgendaCardDropdown = true,
        toggleAgendaCardDropdownVisibility = {},
        visibleAgendaCardDropdownId = null,
        setVisibleAgendaItemId = { _ -> },
        needlePosition = ZonedDateTime.now(),
        toggleTaskComplete = {},
        isTaskChecked = true,
        setTaskCompleteForAgendaItemId = { _ -> },
        taskCompleteForAgendaItemId = ""
    )
}

fun DataError.toLogOutErrorMessage(context: Context): String {
    return when (this) {
        DataError.Network.UNAUTHORIZED -> context.getString(R.string.invalid_or_missing_token_or_api_key)
        DataError.Network.NO_INTERNET -> context.getString(R.string.no_internet_connection)
        else -> context.getString(R.string.unknown_error)
    }
}