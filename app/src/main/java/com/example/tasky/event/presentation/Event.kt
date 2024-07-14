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
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
    EventContent()
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
        EventToolbar(
            cancelEventCreation = { /*TODO*/ },
            saveEventEdits = { /*TODO*/ },
            startEditMode = { /*TODO*/ },
            isEditing = true
        )
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
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
                    .padding(top = 32.dp, bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ColorBlockTypeEvent()
                Spacer(modifier = Modifier.padding(top = 10.dp))
                CheckboxHeader()
                GrayDivider()
                EventDescription()
                EmptyPhotos()
                GrayDivider()
                StartDateLineItem()
                GrayDivider()
                EndDateLineItem()
                GrayDivider()
            }
        }
    }
}

@Composable
@Preview
fun ColorBlockTypeEvent() {
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
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
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 24.dp)
    ) {
        CustomCheckbox(isChecked = false, color = Color.Black, size = 20.dp)
        HeaderLargeStrikeThrough(
            title = stringResource(R.string.new_event),
            isChecked = false,
            textColor = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        RightCarrotIcon()
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
            .padding(16.dp)
    )
}

@Composable
@Preview
fun EventDescription() {
    Row(
        modifier = Modifier.padding(start = 16.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.event_description),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        RightCarrotIcon()
    }
}

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
fun DateLineItem() {
    val isEditing = true
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 6.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.from),
            fontSize = 16.sp
        )
        Text(
            text = "08:00",
            fontSize = 16.sp
        ) // TODO timePicker

        if (isEditing == true) {
            RightCarrotIcon()
        }

        Text(
            text = "Jul 21 2024",
            fontSize = 16.sp
        )// TODO CalendarPicker

        if (isEditing == true) {
            RightCarrotIcon()
        }
    }
}

@Composable
@Preview
fun StartDateLineItem() {
    val isEditing = true
    DateLineItem()
}

@Composable
@Preview
fun EndDateLineItem() {
    val isEditing = false
    DateLineItem()
}

@Composable
fun RightCarrotIcon() {
    Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = null,
        tint = Color.Black
    )
}