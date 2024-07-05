package com.example.tasky.feature_agenda.presentation

import HorizontalCalendar
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.tasky.AuthNavRoute
import com.example.tasky.R
import com.example.tasky.common.domain.error.DataError
import com.example.tasky.common.presentation.CreateErrorAlertDialog
import com.example.tasky.common.presentation.LoadingSpinner
import com.example.tasky.common.presentation.ObserveAsEvents
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import com.vanpra.composematerialdialogs.MaterialDialogState
import java.time.LocalDate

@Composable
fun AgendaRoot(
    navController: NavController,
    agendaViewModel: AgendaViewModel = hiltViewModel()
) {
    val viewState by agendaViewModel.viewState.collectAsStateWithLifecycle()

    val showDialog by agendaViewModel.showDialog.collectAsStateWithLifecycle()
    val initials by agendaViewModel.initials.collectAsStateWithLifecycle()
    val showLogoutDropdown by agendaViewModel.showLogoutDropdown.collectAsStateWithLifecycle()
    val monthSelected by agendaViewModel.monthSelected.collectAsStateWithLifecycle()
    val daySelected by agendaViewModel.daySelected.collectAsStateWithLifecycle()
    val yearSelected by agendaViewModel.yearSelected.collectAsStateWithLifecycle()
    val dialogState by agendaViewModel.dateDialogState.collectAsStateWithLifecycle()
    val calendarDays by agendaViewModel.calendarDays.collectAsStateWithLifecycle()
    //todo use viewevents?
    agendaViewModel.getCalendarDaysForMonth(year = yearSelected, month = monthSelected)

    AgendaContent(
        calendarDays = calendarDays,
        monthSelected = monthSelected,
        daySelected = daySelected,
        yearSelected = yearSelected,
        initials = initials,
        updateDateSelected = { agendaViewModel.updateDateSelected(it) },
        onProfileClick = { agendaViewModel.toggleLogoutDropdownVisibility() },
        showLogoutDropdown = showLogoutDropdown,
        onDismissRequest = { agendaViewModel.toggleLogoutDropdownVisibility() },
        dialogState = dialogState,
        updateDateDialogState = { agendaViewModel.updateDateDialogState(it) },
        onLogoutClick = { agendaViewModel.logOutClicked() }
    )

    when (viewState) {
        is AgendaViewState.LoadingSpinner -> {
            // Show a loading indicator
            LoadingSpinner()
        }

        is AgendaViewState.ErrorDialog -> {
            // Show an Alert Dialog with API failure Error code/message
            val message =
                (viewState as AgendaViewState.ErrorDialog).dataError.toLogOutErrorMessage(
                    context = LocalContext.current
                )

            CreateErrorAlertDialog(
                showDialog = showDialog,
                dialogMessage = message,
                onDismiss = { agendaViewModel.onErrorDialogDismissed() }
            )
        }

        null -> println("no action")
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
    yearSelected: Int,
    initials: String,
    updateDateSelected: (LocalDate) -> Unit,
    onProfileClick: () -> Unit,
    showLogoutDropdown: Boolean,
    onDismissRequest: () -> Unit,
    dialogState: MaterialDialogState,
    updateDateDialogState: (MaterialDialogState) -> Unit,
    onLogoutClick: () -> Unit
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
            onDateSelected = updateDateSelected,
            onProfileClick = onProfileClick,
            showLogoutDropdown = showLogoutDropdown,
            onDismissRequest = onDismissRequest,
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalCalendar(
                    calendarDays = calendarDays,
                    month = monthSelected,
                    day = daySelected,
                    year = yearSelected,
                    updateDateSelected = updateDateSelected
                )
            }
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
        yearSelected = 2024,
        initials = "AB",
        updateDateSelected = { },
        onProfileClick = { },
        showLogoutDropdown = true,
        onDismissRequest = { },
        dialogState = MaterialDialogState(),
        updateDateDialogState = { },
        onLogoutClick = { }
    )
}

fun DataError.toLogOutErrorMessage(context: Context): String {
    return when (this) {
        DataError.Network.UNAUTHORIZED -> context.getString(R.string.invalid_or_missing_token_or_api_key)
        DataError.Network.NO_INTERNET -> context.getString(R.string.no_internet_connection)
        else -> context.getString(R.string.unknown_error)
    }
}