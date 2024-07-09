package com.example.tasky.feature_agenda.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.common.presentation.LogoutDropdownRoot
import com.example.tasky.feature_agenda.presentation.datepicker.MonthPickerOnToolbar
import com.vanpra.composematerialdialogs.MaterialDialogState

@Composable
fun AgendaToolbar(
    monthSelected: Int,
    initials: String,
    updateDateSelected: (Int, Int, Int) -> Unit,
    toggleLogoutDropdownVisibility: () -> Unit,
    showLogoutDropdown: Boolean,
    dialogState: MaterialDialogState,
    onDialogStateChange: (MaterialDialogState) -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MonthPickerOnToolbar(
            monthSelected = monthSelected,
            updateDateSelected = updateDateSelected,
            dialogState = dialogState,
            onDialogStateChange = onDialogStateChange
        )
        ProfileIcon(
            initials = initials,
            toggleLogoutDropdownVisibility = toggleLogoutDropdownVisibility,
            showDropdown = showLogoutDropdown,
            onLogoutClick = onLogoutClick
        )
    }
}

@Composable
fun ProfileIcon(
    initials: String,
    toggleLogoutDropdownVisibility: () -> Unit,
    showDropdown: Boolean,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
            .padding(top = 8.dp, bottom = 8.dp)
            .background(color = colorResource(id = R.color.light_gray), shape = CircleShape)
            .size(34.dp)
            .clickable { toggleLogoutDropdownVisibility() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = colorResource(id = R.color.light_blue),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        LogoutDropdownRoot(
            showLogoutDropdown = showDropdown,
            toggleLogoutDropdownVisibility = toggleLogoutDropdownVisibility,
            onLogoutClick = onLogoutClick,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
@Preview
fun PreviewProfileIcon() {
    ProfileIcon(
        initials = "AB",
        toggleLogoutDropdownVisibility = { },
        showDropdown = true,
        onLogoutClick = { }
    )
}

@Composable
@Preview
fun PreviewAgendaToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MonthPickerOnToolbar(
            3,
            updateDateSelected = { month, day, _ -> },
            dialogState = MaterialDialogState(),
            onDialogStateChange = { }
        )
        ProfileIcon(
            initials = "AB",
            toggleLogoutDropdownVisibility = { },
            showDropdown = true,
            onLogoutClick = { }
        )
    }
}