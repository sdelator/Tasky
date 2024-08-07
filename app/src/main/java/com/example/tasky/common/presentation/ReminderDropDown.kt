package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasky.R

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
            text = { Text(stringResource(id = ReminderTime.TEN_MINUTES.typeName)) },
            onClick = { onReminderClick(ReminderTime.TEN_MINUTES) }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = ReminderTime.THIRTY_MINUTES.typeName)) },
            onClick = { onReminderClick(ReminderTime.THIRTY_MINUTES) }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = ReminderTime.ONE_HOUR.typeName)) },
            onClick = { onReminderClick(ReminderTime.ONE_HOUR) }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = ReminderTime.SIX_HOURS.typeName)) },
            onClick = { onReminderClick(ReminderTime.SIX_HOURS) }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = ReminderTime.ONE_DAY.typeName)) },
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

enum class ReminderTime(val typeName: Int, val epochMilliSeconds: Long) {
    TEN_MINUTES(R.string.ten_minutes_before, 600000),
    THIRTY_MINUTES(R.string.thirty_minutes_before, 1800000),
    ONE_HOUR(R.string.one_hour_before, 3600000),
    SIX_HOURS(R.string.six_hours_before, 21600000),
    ONE_DAY(R.string.one_day_before, 86400000)
}