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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.tasky.R
import com.example.tasky.agenda_details.domain.model.AttendeeBasicInfoDetails
import com.example.tasky.agenda_details.presentation.AttendeeFilter
import com.example.tasky.common.presentation.HeaderMedium
import com.example.tasky.common.presentation.PillButton
import com.example.tasky.common.presentation.TextBox

@Composable
fun AttendeeSection(
    isEditMode: Boolean,
    attendeeFilter: AttendeeFilter,
    onAttendeeFilterClick: (AttendeeFilter) -> Unit,
    visitorEmail: String,
    isVisitorEmailValid: Boolean,
    onVisitorEmailChange: (String) -> Unit,
    onToggleVisitorDialog: () -> Unit,
    onAddVisitorClick: () -> Unit,
    isAddVisitorDialogVisible: Boolean,
    visitorList: List<AttendeeBasicInfoDetails>,
    showVisitorDoesNotExist: Boolean
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
                AddVisitorButton(
                    onToggleVisitorDialog = onToggleVisitorDialog
                )
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        AttendeeStatusSection(
            attendeeFilter = attendeeFilter,
            onAttendeeFilterClick = onAttendeeFilterClick
        )
        GoingSection(
            headerText = stringResource(R.string.going),
            visitorList = visitorList
        )
        GoingSection(
            headerText = stringResource(R.string.not_going),
            visitorList = visitorList
        )
    }

    if (isAddVisitorDialogVisible) {
        AddVisitorDialog(
            email = visitorEmail,
            isEmailValid = isVisitorEmailValid,
            onEmailChange = onVisitorEmailChange,
            onDismiss = onToggleVisitorDialog,
            onAddVisitorClick = onAddVisitorClick,
            showVisitorDoesNotExist = showVisitorDoesNotExist
        )
    }
}

@Composable
fun AddVisitorButton(onToggleVisitorDialog: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .background(colorResource(id = R.color.reminder_gray))
                .clickable(onClick = { onToggleVisitorDialog() }),
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
fun AddVisitorDialog(
    email: String,
    isEmailValid: Boolean,
    onEmailChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onAddVisitorClick: () -> Unit,
    showVisitorDoesNotExist: Boolean
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.cancel)
                    )
                }
                AddVisitorHeader(modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.padding(bottom = 10.dp))
                TextBox(
                    hintText = stringResource(R.string.email_address),
                    text = email,
                    isValid = isEmailValid,
                    onValueChange = onEmailChange
                )

                if (showVisitorDoesNotExist) {
                    Text(
                        text = stringResource(id = R.string.visitor_does_not_exist),
                        color = Color.Red,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(bottom = 3.dp))
                AddButton(
                    enabled = isEmailValid,
                    onClick = { onAddVisitorClick() }
                )
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
    }
}

@Composable
fun AddVisitorHeader(modifier: Modifier) {
    Text(
        text = stringResource(R.string.add_visitor),
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 20.sp,
        modifier = modifier
    )
}

@Composable
fun AddButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Text(stringResource(R.string.add))
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
        onAttendeeFilterClick = {},
        visitorEmail = "",
        isVisitorEmailValid = true,
        onToggleVisitorDialog = {},
        onVisitorEmailChange = {},
        onAddVisitorClick = {},
        isAddVisitorDialogVisible = true,
        visitorList = listOf(),
        showVisitorDoesNotExist = true
    )
}

@Preview
@Composable
fun PreviewAttendeeDialog() {
    AddVisitorDialog(
        email = "test",
        isEmailValid = true,
        onEmailChange = {},
        onDismiss = {},
        onAddVisitorClick = {},
        showVisitorDoesNotExist = false
    )
}