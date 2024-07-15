package com.example.tasky.common.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasky.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerColors
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
@Preview
fun PreviewTimePicker() {
    val dialogState = rememberMaterialDialogState()
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
        timepicker(
            colors = customTimePickerColors()
        ) {
            dialogState.hide()
        }
    }
    /* This should be called in an onClick or an Effect */
    dialogState.show()
}

@Composable
fun customTimePickerColors(): TimePickerColors {
    return TimePickerDefaults.colors(
        activeBackgroundColor = colorResource(id = R.color.tasky_green),
        inactiveBackgroundColor = colorResource(id = R.color.reminder_gray),
        activeTextColor = Color.White,
        inactiveTextColor = Color.Black,
        inactivePeriodBackground = colorResource(id = R.color.gray),
        selectorColor = colorResource(id = R.color.tasky_green),
        selectorTextColor = Color.White,
        headerTextColor = Color.Black,
        borderColor = Color.Black
    )
}