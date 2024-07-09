package com.example.tasky.feature_agenda.presentation

import HorizontalCalendar
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.tasky.common.presentation.HeaderSmall
import com.example.tasky.common.presentation.LoadingSpinner
import com.example.tasky.common.presentation.ObserveAsEvents
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import com.vanpra.composematerialdialogs.MaterialDialogState

@Composable
fun AgendaRoot(
    navController: NavController,
    agendaViewModel: AgendaViewModel = hiltViewModel()
) {
    val viewState by agendaViewModel.viewState.collectAsStateWithLifecycle()
    val initials = agendaViewModel.initials

    val updateDateSelected: (Int, Int, Int?) -> Unit = { month, day, year ->
        agendaViewModel.updateDateSelected(month, day, year)
    }

    AgendaContent(
        calendarDays = viewState.calendarDays,
        monthSelected = viewState.monthSelected,
        daySelected = viewState.daySelected,
        initials = initials,
        updateDateSelected = updateDateSelected,
        toggleLogoutDropdownVisibility = { agendaViewModel.toggleLogoutDropdownVisibility() },
        showLogoutDropdown = viewState.showLogoutDropdown,
        dialogState = viewState.datePickerDialogState,
        updateDateDialogState = { agendaViewModel.updateDateDialogState(it) },
        onLogoutClick = { agendaViewModel.logOutClicked() },
        checkAuthenticationClick = { agendaViewModel.checkAuthentication() }
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
    showLogoutDropdown: Boolean,
    dialogState: MaterialDialogState,
    updateDateDialogState: (MaterialDialogState) -> Unit,
    onLogoutClick: () -> Unit,
    checkAuthenticationClick: () -> Unit
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
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
                HeaderSmall(title = "Today", modifier = Modifier.align(Alignment.Start))
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
        initials = "AB",
        updateDateSelected = { month, day, year -> },
        toggleLogoutDropdownVisibility = { },
        showLogoutDropdown = true,
        dialogState = MaterialDialogState(),
        updateDateDialogState = { },
        onLogoutClick = { },
        checkAuthenticationClick = { }
    )
}

fun DataError.toLogOutErrorMessage(context: Context): String {
    return when (this) {
        DataError.Network.UNAUTHORIZED -> context.getString(R.string.invalid_or_missing_token_or_api_key)
        DataError.Network.NO_INTERNET -> context.getString(R.string.no_internet_connection)
        else -> context.getString(R.string.unknown_error)
    }
}