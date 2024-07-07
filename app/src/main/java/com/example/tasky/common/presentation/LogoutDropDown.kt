package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tasky.R

@Composable
fun LogoutDropdownRoot(
    showLogoutDropdown: Boolean,
    toggleLogoutDropdownVisibility: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier
) {
    DropdownMenu(
        expanded = showLogoutDropdown,
        onDismissRequest = toggleLogoutDropdownVisibility,
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.logout)) },
            onClick = {
                toggleLogoutDropdownVisibility()
                onLogoutClick()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDropdownMenu() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LogoutDropdownRoot(
            showLogoutDropdown = true,
            toggleLogoutDropdownVisibility = { },
            onLogoutClick = { },
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewDropdownMenuItem() {
    DropdownMenuItem(
        text = { Text(stringResource(R.string.logout)) },
        onClick = { }
    )
}