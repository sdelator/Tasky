package com.example.tasky.common.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R

@Composable
fun ReminderSection(
    reminderTime: ReminderTime,
    showReminderDropdown: Boolean,
    toggleReminderDropdownVisibility: () -> Unit,
    onReminderClick: (ReminderTime) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { toggleReminderDropdownVisibility() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_reminder),
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = stringResource(id = reminderTime.typeName),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        ReminderDropdownRoot(
            showReminderDropdown = showReminderDropdown,
            toggleReminderDropdownVisibility = toggleReminderDropdownVisibility,
            Modifier.align(Alignment.TopEnd),
            onReminderClick = onReminderClick
        )
    }
}