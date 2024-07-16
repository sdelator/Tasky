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
import java.time.LocalTime

@Composable
fun DateLineItem(
    isEditing: Boolean,
    dialogState: MaterialDialogState,
    timeDialogState: MaterialDialogState,
    updateDateDialogState: (MaterialDialogState, LineItemType) -> Unit,
    updateTimeDialogState: (MaterialDialogState, LineItemType) -> Unit,
    updateTimeSelected: (LocalTime, LineItemType) -> Unit,
    buttonType: LineItemType,
    time: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (buttonType == LineItemType.FROM) stringResource(id = R.string.from)
            else stringResource(id = R.string.to),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        CustomTimePicker(
            modifier = Modifier.weight(1f),
            dialogState = timeDialogState,
            updateTimeDialogState = updateTimeDialogState,
            updateTimeSelected = updateTimeSelected,
            buttonType = buttonType,
            time = time
        )

        if (isEditing) RightCarrotIcon(modifier = Modifier.weight(1f))
        else InvisibleRightCarrotIcon(modifier = Modifier.weight(1f))

        DatePickerLineItem(
            dialogState = dialogState,
            modifier = Modifier
                .weight(2f)
                .padding(start = 16.dp),
            updateDateDialogState = updateDateDialogState,
            lineItemType = buttonType
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
            isEditing = true,
            dialogState = MaterialDialogState(),
            timeDialogState = MaterialDialogState(),
            updateDateDialogState = { _, _ -> },
            updateTimeDialogState = { _, _ -> },
            updateTimeSelected = { _, _ -> },
            buttonType = LineItemType.TO,
            time = ""
        )
        DateLineItem(
            isEditing = false,
            dialogState = MaterialDialogState(),
            timeDialogState = MaterialDialogState(),
            updateDateDialogState = { _, _ -> },
            updateTimeDialogState = { _, _ -> },
            updateTimeSelected = { _, _ -> },
            buttonType = LineItemType.FROM,
            time = ""
        )
    }
}