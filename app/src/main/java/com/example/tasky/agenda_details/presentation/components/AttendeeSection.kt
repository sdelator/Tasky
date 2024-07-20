package com.example.tasky.agenda_details.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tasky.R
import com.example.tasky.agenda_details.presentation.AttendeeFilter
import com.example.tasky.common.presentation.HeaderMedium
import com.example.tasky.common.presentation.PillButton

@Composable
fun AttendeeSection(
    attendeeFilter: AttendeeFilter,
    onAttendeeFilterClick: (AttendeeFilter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
    ) {
        HeaderMedium(
            title = stringResource(R.string.visitors),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            textColor = Color.Black
        )
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
            buttonName = AttendeeFilter.ALL.typeName,
            onClick = { onAttendeeFilterClick(AttendeeFilter.ALL) },
            isSelected = attendeeFilter == AttendeeFilter.ALL
        )
        PillButton(
            buttonName = AttendeeFilter.GOING.typeName,
            onClick = { onAttendeeFilterClick(AttendeeFilter.GOING) },
            isSelected = attendeeFilter == AttendeeFilter.GOING
        )
        PillButton(
            buttonName = AttendeeFilter.NOT_GOING.typeName,
            onClick = { onAttendeeFilterClick(AttendeeFilter.NOT_GOING) },
            isSelected = attendeeFilter == AttendeeFilter.NOT_GOING
        )
    }
}