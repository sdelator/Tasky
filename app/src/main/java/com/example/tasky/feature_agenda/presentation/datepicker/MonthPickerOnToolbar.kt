package com.example.tasky.feature_agenda.presentation.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthPickerOnToolbar() {
    // Initial state setup for the DatePickerDialog. Specifies to show the picker initially
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    // State to hold the selected date as a String
    val selectedDateLabel = remember { mutableStateOf("") }
    // State to control the visibility of the DatePickerDialog
    val openDialog = remember { mutableStateOf(false) }
    // Define the main color for the calendar picker
    val calendarPickerMainColor = Color(0xFF722276)

    // Layout for displaying the button and the selected date
    Column(
        modifier = Modifier.background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Button to open the DatePickerDialog
        MonthIconSelectionText(
            monthSelected = selectedDateLabel.value,
            onClick = {
                openDialog.value = !openDialog.value
            }
        )
    }

    // Conditional display of the DatePickerDialog based on the openDialog state
    if (openDialog.value) {
        // DatePickerDialog component with custom colors and button behaviors
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFFF5F0FF),
            ),
            onDismissRequest = {
                // Action when the dialog is dismissed without selecting a date
                openDialog.value = false
            },
            confirmButton = {
                // Confirm button with custom action and styling
                TextButton(
                    onClick = {
                        // Action to set the selected date and close the dialog
                        openDialog.value = false
                        selectedDateLabel.value =
                            datePickerState.selectedDateMillis?.convertMillisToDate() ?: ""
                    }
                ) {
                    Text("OK", color = calendarPickerMainColor)
                }
            },
            dismissButton = {
                // Dismiss button to close the dialog without selecting a date
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("CANCEL", color = calendarPickerMainColor)
                }
            }
        ) {
            // The actual DatePicker component within the dialog
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

fun Long.convertMillisToDate(): String {
    // Create a calendar instance in the default time zone
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@convertMillisToDate
        // Adjust for the time zone offset to get the correct local date
        val zoneOffset = get(Calendar.ZONE_OFFSET)
        val dstOffset = get(Calendar.DST_OFFSET)
        add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
    }
    // Format the calendar time in the specified format
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    return sdf.format(calendar.time)
}

@Composable
fun MonthIconSelectionText(
    monthSelected: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = Color.Black)
            .clickable { onClick() }
    ) {
        Text(
            text = monthSelected,
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
    MonthIconSelectionText(
        monthSelected = "MARCH",
        onClick = { }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewDatePicker() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
        DatePicker(state = state, modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
fun PreviewMonthPickerToolbar() {
    MonthPickerOnToolbar()
}