package com.example.tasky.event.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tasky.R
import com.example.tasky.common.presentation.CustomCheckbox
import com.example.tasky.common.presentation.HeaderLargeStrikeThrough

@Composable
fun EventRoot(
    navController: NavController,
    eventViewModel: EventViewModel = hiltViewModel()
) {

}

@Composable
@Preview
fun EventContent(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        //TODO add EventToolbar
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 75.dp),
            shape = RoundedCornerShape(
                topStart = 30.dp, topEnd = 30.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ColorBlockTypeEvent()
                Spacer(modifier = Modifier.padding(top = 10.dp))
                CheckboxHeader()
                GrayDivider()
                EventDescription()
                GrayDivider()
            }
        }
    }
}

@Composable
@Preview
fun ColorBlockTypeEvent() {
    Row {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(shape = RoundedCornerShape(2.dp))
                .background(colorResource(id = R.color.event_light_green)),
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(id = R.string.event),
            color = colorResource(id = R.color.dark_gray)
        )
    }
}

@Composable
@Preview
fun CheckboxHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 16.dp, end = 16.dp)
    ) {
        CustomCheckbox(isChecked = false, color = Color.Black, size = 20.dp)
        HeaderLargeStrikeThrough(
            title = "Meeting",
            isChecked = false,
            textColor = Color.Black
        )
    }
}

@Composable
@Preview
fun GrayDivider() {
    Divider(
        color = colorResource(id = R.color.light_gray),
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    )
}

@Composable
@Preview
fun EventDescription() {
    Text(
        text = "Test testing 123...",
        fontSize = 16.sp
    )
}

@Composable
@Preview
fun EmptyPhotos() {
    Box(
        Modifier
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