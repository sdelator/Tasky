package com.example.tasky.common.presentation

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerColors
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun CustomTimePicker(
    modifier: Modifier,
    dialogState: MaterialDialogState,
    updateTimeDialogState: (MaterialDialogState) -> Unit
) {
    Text(
        text = "08:00",
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
        timepicker(
            colors = customTimePickerColors()
        ) {
            dialogState.hide()
            updateTimeDialogState(dialogState)
        }
    }
}

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
        activeBackgroundColor = colorResource(id = R.color.sea_foam_green),
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