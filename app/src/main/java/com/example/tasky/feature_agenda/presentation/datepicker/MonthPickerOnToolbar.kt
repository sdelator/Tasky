package com.example.tasky.feature_agenda.presentation.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.common.domain.util.convertMonthToString
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun MonthTextWithIcon(
    monthSelected: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = Color.Black)
            .clickable { onClick() }
    ) {
        Text(
            text = monthSelected.convertMonthToString(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier
                .background(Color.Black),
            tint = Color.White
        )
    }
}

@Composable
@Preview
fun PreviewMonthIconSelectionText() {
    MonthTextWithIcon(
        monthSelected = 3,
        onClick = { }
    )
}

@Composable
fun MonthPickerOnToolbar(
    monthSelected: Int,
    onDateSelected: (LocalDate) -> Unit,
    dialogState: MaterialDialogState,
    onDialogStateChange: (MaterialDialogState) -> Unit
) {

    // Button to open the DatePickerDialog
    MonthTextWithIcon(
        monthSelected = monthSelected,
        onClick = {
            dialogState.show()
            onDialogStateChange(dialogState)
        }
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
            onDateSelected(date)
            dialogState.hide()
            onDialogStateChange(dialogState)
        }
    }
}

@Composable
fun customDatePickerColors(): com.vanpra.composematerialdialogs.datetime.date.DatePickerColors {
    return com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults.colors(
        headerBackgroundColor = colorResource(id = R.color.tasky_green),
        headerTextColor = Color.White,
        calendarHeaderTextColor = Color.Black,
        dateActiveBackgroundColor = colorResource(id = R.color.sea_foam_green),
        dateInactiveBackgroundColor = Color.White,
        dateActiveTextColor = Color.Black,
        dateInactiveTextColor = Color.Black
    )
}

@Composable
@Preview
fun CalendarDatePicker() {
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(stringResource(id = R.string.ok))
            negativeButton(stringResource(id = R.string.cancel))
        }
    ) {
        datepicker(
            yearRange = IntRange(2023, 2030)
        ) { date ->
            dialogState.hide()
        }
    }
    /* This should be called in an onClick or an Effect */
    dialogState.show()
}

//TODO not used, but maybe in future use Material3 date picker
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material3MonthPickerOnToolbar(
    monthSelected: Int,
    openDatePickerDialog: Boolean,
    onMonthClick: () -> Unit,
    onMonthSelected: (Int) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val selectedDateLabel = remember { mutableStateOf(monthSelected) }
    val calendarPickerMainColor = Color(0xFF722276)

    Column(
        modifier = Modifier.background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Button to open the DatePickerDialog
        MonthTextWithIcon(
            monthSelected = selectedDateLabel.value,
            onClick = onMonthClick
        )
    }

    if (openDatePickerDialog) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFFF5F0FF),
            ),
            onDismissRequest = onMonthClick,
            confirmButton = {
                TextButton(
                    onClick = {
                        // Action to set the selected date and close the dialog
                        selectedDateLabel.value = 3
//                            datePickerState.selectedDateMillis?.convertMillisToMonth() ?: ""
                        onMonthSelected(selectedDateLabel.value)
                        onMonthClick()
                    }
                ) {
                    Text(stringResource(id = R.string.ok), color = calendarPickerMainColor)
                }
            },
            dismissButton = {
                TextButton(onClick = onMonthClick) {
                    Text(stringResource(R.string.cancel), color = calendarPickerMainColor)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = calendarPickerMainColor,
                    selectedDayContentColor = Color.White,
                    selectedYearContainerColor = calendarPickerMainColor,
                    selectedYearContentColor = Color.White,
                    todayContentColor = calendarPickerMainColor,
                    todayDateBorderColor = calendarPickerMainColor
                )
            )
        }
    }
}

@Composable
@Preview
fun Material3ComposableDatePicker() {
    Material3MonthPickerOnToolbar(
        monthSelected = 3,
        openDatePickerDialog = true,
        onMonthClick = { /*TODO*/ }) {
    }
}