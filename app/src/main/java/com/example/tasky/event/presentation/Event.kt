package com.example.tasky.event.presentation

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tasky.R
import com.example.tasky.common.presentation.HeaderSmall
import com.example.tasky.common.presentation.model.Action
import com.example.tasky.feature_agenda.presentation.AgendaToolbar
import com.example.tasky.feature_agenda.presentation.model.CalendarDay
import com.vanpra.composematerialdialogs.MaterialDialogState

@Composable
fun EventRoot(
    navController: NavController,
    eventViewModel: EventViewModel = hiltViewModel()
) {

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
    onFabActionClick: (Action) -> Unit,
    headerDateText: String
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
                ColorBlockTypeEvent()
                Spacer(modifier = Modifier.padding(top = 10.dp))
                HeaderSmall(title = headerDateText, modifier = Modifier.align(Alignment.Start))
            }
        }
    }
}

@Composable
@Preview
fun ColorBlockTypeEvent() {
    Row {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(shape = RoundedCornerShape(2.dp))
                .background(colorResource(id = R.color.event_light_green)),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(id = R.string.event),
            color = colorResource(id = R.color.dark_gray)
        )
    }
}