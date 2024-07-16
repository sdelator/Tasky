package com.example.tasky.common.presentation

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.feature_agenda.presentation.datepicker.customDatePickerColors
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker

@Composable
fun DatePickerLineItem(
    modifier: Modifier,
    dialogState: MaterialDialogState,
    updateDateDialogState: (MaterialDialogState, LineItemType) -> Unit,
    lineItemType: LineItemType
) {
    Text(
        text = "Jul 21 2024",
        fontSize = 16.sp,
        modifier = modifier
            .clickable { dialogState.show() }
    )

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(
                stringResource(id = R.string.ok),
                textStyle = TextStyle(color = Color.Black)
            )
            negativeButton(
                stringResource(id = R.string.cancel),
                textStyle = TextStyle(color = Color.Black)
            )
        }
    ) {
        datepicker(
            yearRange = IntRange(2023, 2030),
            colors = customDatePickerColors()
        ) { date ->
            //updateDateSelected(date.monthValue, date.dayOfMonth, date.year)
            dialogState.hide()
            updateDateDialogState(dialogState, lineItemType)
        }
    }
}