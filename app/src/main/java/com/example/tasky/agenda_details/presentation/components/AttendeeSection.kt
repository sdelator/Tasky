package com.example.tasky.agenda_details.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.agenda_details.presentation.AttendeeFilter
import com.example.tasky.common.presentation.HeaderMedium
import com.example.tasky.common.presentation.PillButton

@Composable
@Preview
fun AttendeeSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
    ) {
        HeaderMedium(
            title = "Visitors",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            textColor = Color.Black
        )
        Spacer(modifier = Modifier.padding(16.dp))
        AttendeeStatusSection()
        GoingSection()
//        NotGoingSection()
    }
}

@Composable
fun AttendeeStatusSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        PillButton(buttonName = AttendeeFilter.ALL.typeName, onClick = { /*TODO*/ })
        PillButton(buttonName = AttendeeFilter.GOING.typeName, onClick = { /*TODO*/ })
        PillButton(buttonName = AttendeeFilter.NOT_GOING.typeName, onClick = { /*TODO*/ })
    }
}