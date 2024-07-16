package com.example.tasky.actions.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R

@Composable
fun EmptyPhotos() {
    Column(
        modifier = Modifier.padding(top = 30.dp, bottom = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .background(colorResource(id = R.color.reminder_gray))
                .padding(top = 50.dp, bottom = 50.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Filled.Add,
                    modifier = Modifier.size(30.dp),
                    tint = colorResource(id = R.color.gray),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Add Photos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.gray)
                )
            }
        }
    }
}