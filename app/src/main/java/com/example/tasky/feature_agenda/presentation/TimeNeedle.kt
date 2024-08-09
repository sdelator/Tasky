package com.example.tasky.feature_agenda.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasky.R

@Preview
@Composable
fun Needle() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = R.drawable.ic_circle), contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Divider(
            color = Color.Black,
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}