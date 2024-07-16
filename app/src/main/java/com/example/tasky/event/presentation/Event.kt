package com.example.tasky.event.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tasky.R
import com.example.tasky.common.domain.util.convertMillisToDate
import com.example.tasky.common.presentation.CheckboxHeader
import com.example.tasky.common.presentation.DateLineItem
import com.example.tasky.common.presentation.GrayDivider
import com.example.tasky.common.presentation.RightCarrotIcon
import com.example.tasky.event.presentation.components.EmptyPhotos

@Composable
fun EventRoot(
    navController: NavController,
    date: Long,
    eventViewModel: EventViewModel = hiltViewModel()
) {
//    val viewState by eventViewModel.viewState.collectAsStateWithLifecycle()
    EventContent(
        dateOnToolbar = date.convertMillisToDate(),
        onToolbarAction = {}
    )
}

@Composable
fun EventContent(
    dateOnToolbar: String,
    onToolbarAction: (ToolbarAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        EventToolbar(
            date = dateOnToolbar,
            onToolbarAction = onToolbarAction,
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
                EmptyPhotos() // TODO if statement for added photos
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
@Preview
fun StartDateLineItem() {
    DateLineItem(
        text = stringResource(id = R.string.from),
        isEditing = false // TODO use viewState to control this
    )
}

@Composable
@Preview
fun EndDateLineItem() {
    DateLineItem(
        text = stringResource(id = R.string.to),
        isEditing = true // TODO use viewState to control this
    )
}