package com.example.tasky.agenda_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.agenda_details.presentation.AttendeeFilter
import com.example.tasky.common.presentation.HeaderMedium
import com.example.tasky.common.presentation.PillButton

@Composable
fun AttendeeSection(
    isEditMode: Boolean,
    attendeeFilter: AttendeeFilter,
    onAttendeeFilterClick: (AttendeeFilter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HeaderMedium(
                title = stringResource(R.string.visitors),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                textColor = Color.Black
            )
            if (isEditMode) {
                AddAttendeeButton()
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        AttendeeStatusSection(
            attendeeFilter = attendeeFilter,
            onAttendeeFilterClick = onAttendeeFilterClick
        )
        GoingSection(headerText = stringResource(R.string.going))
        GoingSection(headerText = stringResource(R.string.not_going))
    }
}

@Composable
fun AddAttendeeButton() {
    Surface(
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .background(colorResource(id = R.color.reminder_gray))
                .clickable(onClick = { }),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = stringResource(R.string.add_attendee),
                tint = colorResource(id = R.color.gray)
            )
        }
    }
}

@Composable
fun AttendeeStatusSection(
    attendeeFilter: AttendeeFilter,
    onAttendeeFilterClick: (AttendeeFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        PillButton(
            buttonName = stringResource(id = AttendeeFilter.ALL.typeName),
            onClick = { onAttendeeFilterClick(AttendeeFilter.ALL) },
            isSelected = attendeeFilter == AttendeeFilter.ALL,
            modifier = Modifier.weight(1f)
        )
        PillButton(
            buttonName = stringResource(id = AttendeeFilter.GOING.typeName),
            onClick = { onAttendeeFilterClick(AttendeeFilter.GOING) },
            isSelected = attendeeFilter == AttendeeFilter.GOING,
            modifier = Modifier.weight(1f)
        )
        PillButton(
            buttonName = stringResource(id = AttendeeFilter.NOT_GOING.typeName),
            onClick = { onAttendeeFilterClick(AttendeeFilter.NOT_GOING) },
            isSelected = attendeeFilter == AttendeeFilter.NOT_GOING,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun PreviewAttendeeSection() {
    AttendeeSection(
        isEditMode = true,
        attendeeFilter = AttendeeFilter.ALL,
        onAttendeeFilterClick = {}
    )
}