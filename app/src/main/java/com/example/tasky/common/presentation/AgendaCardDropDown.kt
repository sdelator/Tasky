package com.example.tasky.common.presentation

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tasky.agenda_details.domain.model.AgendaItem

@Composable
fun CardDropdownRoot(
    agendaItem: AgendaItem,
    showAgendaCardDropdown: Boolean,
    toggleAgendaCardDropdownVisibility: () -> Unit,
    onAgendaCardActionClick: (AgendaItem, CardAction) -> Unit
) {
    DropdownMenu(
        expanded = showAgendaCardDropdown,
        onDismissRequest = toggleAgendaCardDropdownVisibility,
        modifier = Modifier
    ) {
        DropdownMenuItem(
            text = { Text(CardAction.Open.name) },
            onClick = {
                toggleAgendaCardDropdownVisibility()
                onAgendaCardActionClick(agendaItem, CardAction.Open)
            }
        )
        DropdownMenuItem(
            text = { Text(CardAction.Edit.name) },
            onClick = {
                toggleAgendaCardDropdownVisibility()
                onAgendaCardActionClick(agendaItem, CardAction.Edit)
            }
        )
        DropdownMenuItem(
            text = { Text(CardAction.Delete.name) },
            onClick = {
                toggleAgendaCardDropdownVisibility()
                onAgendaCardActionClick(agendaItem, CardAction.Delete)
            }
        )
    }
}

enum class CardAction {
    Open, Edit, Delete
}