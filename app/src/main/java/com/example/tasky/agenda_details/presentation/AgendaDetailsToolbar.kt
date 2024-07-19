package com.example.tasky.agenda_details.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.R

@Composable
fun AgendaDetailsToolbar(
    date: String,
    onToolbarAction: (ToolbarAction) -> Unit,
    isEditing: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CancelButton(onClick = { onToolbarAction(ToolbarAction.CANCEL) })
        EventDate(date = date)
        if (isEditing) {
            SaveButton(onClick = { onToolbarAction(ToolbarAction.SAVE) })
        } else {
            EditButton(onClick = { onToolbarAction(ToolbarAction.EDIT) })
        }
    }
}

@Composable
fun CancelButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = R.string.cancel),
            tint = Color.White
        )
    }
}

@Composable
fun EditButton(onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.padding(8.dp)) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = stringResource(R.string.edit),
            tint = Color.White
        )
    }
}

@Composable
fun SaveButton(onClick: () -> Unit) {
    Text(
        text = stringResource(id = R.string.save),
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun EventDate(date: String) {
    Text(
        text = date,
        color = Color.White,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
@Preview
fun PreviewIcon() {
    AgendaDetailsToolbar(
        date = "01 March 2024",
        onToolbarAction = { },
        isEditing = true
    )
}