package com.example.tasky.agenda_details.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
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
import com.example.tasky.R
import com.example.tasky.common.presentation.HeaderMedium

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

@Composable
@Preview
fun Photos() {
    Column(
        modifier = Modifier.padding(top = 30.dp, bottom = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .background(colorResource(id = R.color.reminder_gray))
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column {
                HeaderMedium(title = "Photos", textColor = Color.Black)
                Spacer(modifier = Modifier.padding(10.dp))
                // TODO add however many photos
                PhotoSlot()
            }
        }
    }
}

@Composable
@Preview
fun PhotoSlot() {
    Surface(
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.light_blue)),
    ) {
        Box(
            modifier = Modifier
                .size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                stringResource(R.string.floating_action_button),
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }
}