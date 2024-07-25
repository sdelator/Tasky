package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ReminderDropdownRoot(
    showReminderDropdown: Boolean,
    toggleReminderDropdownVisibility: () -> Unit,
    modifier: Modifier,
    onReminderClick: (ReminderTime) -> Unit
) {
    DropdownMenu(
        expanded = showReminderDropdown,
        onDismissRequest = toggleReminderDropdownVisibility,
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = { Text(ReminderTime.TEN_MINUTES.typeName) },
            onClick = { onReminderClick(ReminderTime.TEN_MINUTES) }
        )
        DropdownMenuItem(
            text = { Text(ReminderTime.THIRTY_MINUTES.typeName) },
            onClick = { onReminderClick(ReminderTime.THIRTY_MINUTES) }
        )
        DropdownMenuItem(
            text = { Text(ReminderTime.ONE_HOUR.typeName) },
            onClick = { onReminderClick(ReminderTime.ONE_HOUR) }
        )
        DropdownMenuItem(
            text = { Text(ReminderTime.SIX_HOURS.typeName) },
            onClick = { onReminderClick(ReminderTime.SIX_HOURS) }
        )
        DropdownMenuItem(
            text = { Text(ReminderTime.ONE_DAY.typeName) },
            onClick = { onReminderClick(ReminderTime.ONE_DAY) }
        )
    }
}

@Preview
@Composable
fun PreviewReminderDropdownMenu() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ReminderDropdownRoot(
            showReminderDropdown = true,
            toggleReminderDropdownVisibility = { },
            Modifier.align(Alignment.TopEnd),
            onReminderClick = { }
        )
    }
}

enum class ReminderTime(val typeName: String) {
    TEN_MINUTES("10 minutes before"),
    THIRTY_MINUTES("30 minutes before"),
    ONE_HOUR("1 hour before"),
    SIX_HOURS("6 hours before"),
    ONE_DAY("1 day before")
}