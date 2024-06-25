package com.example.tasky.feature_agenda.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R

@Composable
fun AgendaToolbar(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Red)
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "March", color = Color.White)
        ProfileIcon(
            initials = "AB",
            onProfileClick = onProfileClick
        )
    }
}

@Composable
fun ProfileIcon(
    initials: String = "",
    onProfileClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .background(color = colorResource(id = R.color.light_gray), shape = CircleShape)
            .size(34.dp)
            .clickable { onProfileClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = colorResource(id = R.color.light_blue),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview
fun PreviewProfileIcon() {
    ProfileIcon("AB") { }
}

@Composable
@Preview
fun PreviewAgendaToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Red)
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "March", color = Color.White)
        ProfileIcon(
            initials = "AB",
            onProfileClick = { }
        )
    }
}