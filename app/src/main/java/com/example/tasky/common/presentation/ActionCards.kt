package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R

@Composable
@Preview
fun ActionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        HeaderMedium(title = "Meeting")
        CardDetails(details = "Amet minim mollit non deserunt")
        DateOfAction(date = "Mar 5, 10:30 - Mar 5, 11:00")
    }
}

@Composable
fun CardDetails(details: String) {
    Text(
        text = details,
        modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 30.dp),
        style = TextStyle(
            fontSize = 14.sp,
            color = colorResource(id = R.color.dark_gray)
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun DateOfAction(date: String) {
    Text(
        text = date,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        style = TextStyle(
            fontSize = 14.sp,
            color = colorResource(id = R.color.dark_gray)
        ),
        textAlign = TextAlign.End
    )
}