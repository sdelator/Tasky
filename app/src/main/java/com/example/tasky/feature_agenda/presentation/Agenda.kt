package com.example.tasky.feature_agenda.presentation

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
import androidx.compose.material3.Text
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
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

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
    val dialogState = rememberMaterialDialogState()

    AgendaContent(
        monthSelected = monthSelected,
        initials = initials,
        onMonthSelected = { agendaViewModel.onMonthSelected(it) },
        onProfileClick = { agendaViewModel.toggleLogoutDropdownVisibility() },
        showLogoutDropdown = showLogoutDropdown,
        onDismissRequest = { agendaViewModel.toggleLogoutDropdownVisibility() },
        dialogState = dialogState,
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
    monthSelected: String,
    initials: String,
    onMonthSelected: (String) -> Unit,
    onProfileClick: () -> Unit,
    showLogoutDropdown: Boolean,
    onDismissRequest: () -> Unit,
    dialogState: MaterialDialogState,
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
            onMonthSelected = onMonthSelected,
            onProfileClick = onProfileClick,
            showLogoutDropdown = showLogoutDropdown,
            onDismissRequest = onDismissRequest,
            dialogState = dialogState,
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
                Text("Top Text")
            }
        }
    }
}

@Composable
@Preview
fun PreviewAgendaContent() {
    AgendaContent(
        monthSelected = "MARCH",
        initials = "AB",
        onMonthSelected = { },
        onProfileClick = { },
        showLogoutDropdown = true,
        onDismissRequest = { },
        dialogState = MaterialDialogState(),
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