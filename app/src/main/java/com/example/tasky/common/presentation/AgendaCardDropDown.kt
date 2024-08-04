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
    showCardDropdown: Boolean,
    toggleCardDropdownVisiblity: () -> Unit,
    modifier: Modifier,
    onActionClick: (CardAction) -> Unit
) {
    DropdownMenu(
        expanded = showCardDropdown,
        onDismissRequest = toggleCardDropdownVisiblity,
        modifier = Modifier
    ) {
        DropdownMenuItem(
            text = { Text(CardAction.Open.name) },
            onClick = { onActionClick(CardAction.Open) }
        )
        DropdownMenuItem(
            text = { Text(CardAction.Edit.name) },
            onClick = { onActionClick(CardAction.Edit) }
        )
        DropdownMenuItem(
            text = { Text(CardAction.Delete.name) },
            onClick = { onActionClick(CardAction.Delete) }
        )
    }
}

@Preview
@Composable
fun PreviewAgendaCardDropdownMenu() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CardDropdownRoot(
            showCardDropdown = true,
            toggleCardDropdownVisiblity = { },
            Modifier.align(Alignment.TopEnd),
            onActionClick = { }
        )
    }
}

enum class CardAction {
    Open, Edit, Delete
}