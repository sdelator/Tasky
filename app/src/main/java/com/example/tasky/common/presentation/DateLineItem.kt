package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Arrangement
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
import com.example.tasky.event.presentation.RightCarrotIcon

@Composable
@Preview
fun DateLineItem() {
    val isEditing = true
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 6.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.from),
            fontSize = 16.sp
        )
        Text(
            text = "08:00",
            fontSize = 16.sp
        ) // TODO timePicker

        if (isEditing == true) {
            RightCarrotIcon()
        }

        Text(
            text = "Jul 21 2024",
            fontSize = 16.sp
        )// TODO CalendarPicker

        if (isEditing == true) {
            RightCarrotIcon()
        }
    }
}