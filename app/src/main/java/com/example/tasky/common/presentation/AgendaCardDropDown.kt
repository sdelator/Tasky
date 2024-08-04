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
fun CardDropdownRoot(
    showAgendaCardDropdown: Boolean,
    toggleAgendaCardDropdownVisiblity: () -> Unit,
    modifier: Modifier = Modifier,
    onAgendaCardActionClick: (CardAction) -> Unit
) {
    DropdownMenu(
        expanded = showAgendaCardDropdown,
        onDismissRequest = toggleAgendaCardDropdownVisiblity,
        modifier = Modifier
    ) {
        DropdownMenuItem(
            text = { Text(CardAction.Open.name) },
            onClick = { onAgendaCardActionClick(CardAction.Open) }
        )
        DropdownMenuItem(
            text = { Text(CardAction.Edit.name) },
            onClick = { onAgendaCardActionClick(CardAction.Edit) }
        )
        DropdownMenuItem(
            text = { Text(CardAction.Delete.name) },
            onClick = { onAgendaCardActionClick(CardAction.Delete) }
        )
    }
}

@Preview
@Composable
fun PreviewAgendaCardDropdownMenu() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CardDropdownRoot(
            showAgendaCardDropdown = true,
            toggleAgendaCardDropdownVisiblity = { },
            Modifier.align(Alignment.TopEnd),
            onAgendaCardActionClick = { }
        )
    }
}

enum class CardAction {
    Open, Edit, Delete
}