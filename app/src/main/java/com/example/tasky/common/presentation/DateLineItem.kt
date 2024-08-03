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
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun DateLineItem(
    date: String,
    isEditing: Boolean,
    dialogState: MaterialDialogState,
    timeDialogState: MaterialDialogState,
    onDateSelected: (LocalDate, MaterialDialogState, LineItemType) -> Unit,
    onTimeSelected: (LocalTime, LineItemType, MaterialDialogState) -> Unit,
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
            text = when (buttonType) {
                LineItemType.FROM -> stringResource(id = R.string.from)
                LineItemType.TO -> stringResource(id = R.string.to)
                LineItemType.AT -> stringResource(id = R.string.at)
            },
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )

        CustomTimePicker(
            modifier = Modifier.weight(1f),
            dialogState = timeDialogState,
            onTimeSelected = onTimeSelected,
            buttonType = buttonType,
            time = time
        )

        if (isEditing) RightCarrotIcon(modifier = Modifier.weight(1f))
        else InvisibleRightCarrotIcon(modifier = Modifier.weight(1f))

        DatePickerLineItem(
            date = date,
            dialogState = dialogState,
            modifier = Modifier
                .weight(2f)
                .padding(start = 16.dp),
            onDateSelected = onDateSelected,
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
            date = "Jul 21 2024",
            isEditing = true,
            dialogState = MaterialDialogState(),
            timeDialogState = MaterialDialogState(),
            onDateSelected = { _, _, _ -> },
            onTimeSelected = { _, _, _ -> },
            buttonType = LineItemType.TO,
            time = ""
        )
        DateLineItem(
            date = "Jul 22 2024",
            isEditing = false,
            dialogState = MaterialDialogState(),
            timeDialogState = MaterialDialogState(),
            onDateSelected = { _, _, _ -> },
            onTimeSelected = { _, _, _ -> },
            buttonType = LineItemType.FROM,
            time = ""
        )
    }
}