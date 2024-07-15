package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R

@Composable
fun DateLineItem(text: String, isEditing: Boolean) {
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

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .weight(1f)
        )

        Text(
            text = "Jul 21 2024",
            fontSize = 16.sp,
            modifier = Modifier
                .weight(2f)
                .padding(start = 16.dp)
        ) // TODO CalendarPicker

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Black,
        )
    }
}

@Composable
@Preview
fun PreviewDateLineItem() {
    Column {
        DateLineItem(text = stringResource(id = R.string.to), isEditing = true)
        DateLineItem(text = stringResource(id = R.string.from), isEditing = false)
    }
}