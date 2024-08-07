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
import com.example.tasky.common.domain.model.AgendaItemType

@Composable
fun FabDropdownRoot(
    showFabDropdown: Boolean,
    toggleFabDropdownVisibility: () -> Unit,
    onFabActionClick: (AgendaItemType) -> Unit,
    modifier: Modifier
) {
    DropdownMenu(
        expanded = showFabDropdown,
        onDismissRequest = toggleFabDropdownVisibility,
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.event)) },
            onClick = {
                toggleFabDropdownVisibility()
                onFabActionClick(AgendaItemType.Event)
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.task)) },
            onClick = {
                toggleFabDropdownVisibility()
                onFabActionClick(AgendaItemType.Task)
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.reminder)) },
            onClick = {
                toggleFabDropdownVisibility()
                onFabActionClick(AgendaItemType.Reminder)
            }
        )
    }
}

@Preview
@Composable
fun PreviewFabDropdownMenu() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        FabDropdownRoot(
            showFabDropdown = true,
            toggleFabDropdownVisibility = { },
            onFabActionClick = { },
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
@Preview
fun PreviewFabDropdownMenuItem() {
    DropdownMenuItem(
        text = { Text(stringResource(R.string.event)) },
        onClick = { }
    )
    DropdownMenuItem(
        text = { Text(stringResource(R.string.task)) },
        onClick = { }
    )
    DropdownMenuItem(
        text = { Text(stringResource(R.string.reminder)) },
        onClick = { }
    )
}