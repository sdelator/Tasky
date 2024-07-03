package com.example.tasky.common.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomCheckbox(isChecked = true)
            HeaderMedium(title = "Meeting")
        }
        CardDetails(details = "Amet minim mollit non deserunt")
        DateOfAction(date = "Mar 5, 10:30 - Mar 5, 11:00")
    }
}

@Composable
fun CustomCheckbox(isChecked: Boolean) {
    Icon(
        painter = if (isChecked) {
            painterResource(id = R.drawable.ic_closed_checkbox)
        } else {
            painterResource(id = R.drawable.ic_open_checkbox)
        },
        modifier = Modifier.padding(end = 8.dp),
        contentDescription = "Checkbox circle icon"
    )
}

@Composable
@Preview
fun PreviewCustomCheckbox() {
    CustomCheckbox(isChecked = true)
}

@Composable
fun CardDetails(details: String) {
    Text(
        text = details,
        modifier = Modifier.padding(start = 40.dp, end = 30.dp, bottom = 30.dp),
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
            .padding(top = 10.dp, end = 16.dp, bottom = 10.dp),
        style = TextStyle(
            fontSize = 14.sp,
            color = colorResource(id = R.color.dark_gray)
        ),
        textAlign = TextAlign.End
    )
}