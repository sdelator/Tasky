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
    toggleReminderDropdownVisibility: () -> Unit
) {
    DropdownMenu(
        expanded = showReminderDropdown,
        onDismissRequest = toggleReminderDropdownVisibility
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.ten_minutes_before)) },
            onClick = {
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.thirty_minutes_before)) },
            onClick = {
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.one_hour_before)) },
            onClick = {
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.six_hour_before)) },
            onClick = {
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.one_day_before)) },
            onClick = {
            }
        )
    }
}

@Preview
@Composable
fun PreviewReminderDropdownMenu() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ReminderDropdownRoot(
            showReminderDropdown = true,
            toggleReminderDropdownVisibility = { }
        )
    }
}