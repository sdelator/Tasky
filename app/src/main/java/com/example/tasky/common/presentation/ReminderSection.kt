package com.example.tasky.common.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R

@Composable
@Preview
fun ReminderSection() {
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_reminder),
            contentDescription = null
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = "30 minutes before",
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}