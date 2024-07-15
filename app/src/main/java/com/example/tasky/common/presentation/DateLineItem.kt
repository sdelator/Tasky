package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.vanpra.composematerialdialogs.MaterialDialogState

@Composable
fun DateLineItem(
    text: String,
    isEditing: Boolean,
    dialogState: MaterialDialogState,
    updateDateDialogState: (MaterialDialogState) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "08:00",
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        ) // TODO TimePicker

        if (isEditing) RightCarrotIcon(modifier = Modifier.weight(1f))
        else InvisibleRightCarrotIcon(modifier = Modifier.weight(1f))

        DatePickerLineItem(
            dialogState = dialogState,
            modifier = Modifier
                .weight(2f)
                .padding(start = 16.dp),
            updateDateDialogState = updateDateDialogState
        )

        if (isEditing) RightCarrotIcon()
        else InvisibleRightCarrotIcon()
    }
}

@Composable
@Preview
fun PreviewDateLineItem() {
    Column {
        DateLineItem(
            text = stringResource(id = R.string.to),
            isEditing = true,
            dialogState = MaterialDialogState(),
            updateDateDialogState = { }
        )
        DateLineItem(
            text = stringResource(id = R.string.from),
            isEditing = false,
            dialogState = MaterialDialogState(),
            updateDateDialogState = { }
        )
    }
}