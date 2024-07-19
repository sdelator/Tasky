package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.agenda_details.presentation.AttendeeFilter

@Composable
@Preview
fun AttendeeSection() {
    Column(
        modifier = Modifier.padding(start = 16.dp)
    ) {
        HeaderMedium(title = "Visitors", textColor = Color.Black)
        Spacer(modifier = Modifier.padding(16.dp))
        AttendeeStatusSection()
    }
}

@Composable
fun AttendeeStatusSection() {
    Row {
        PillButton(buttonName = AttendeeFilter.ALL.name, onClick = { /*TODO*/ })
        PillButton(buttonName = AttendeeFilter.GOING.name, onClick = { /*TODO*/ })
        PillButton(buttonName = AttendeeFilter.NOT_GOING.name, onClick = { /*TODO*/ })
    }
}