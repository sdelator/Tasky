package com.example.tasky.agenda_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasky.R
import com.example.tasky.common.presentation.HeaderSmall

@Composable
fun GoingSection(headerText: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HeaderSmall(title = headerText)
        Spacer(modifier = Modifier.padding(bottom = 5.dp))
        //TODO add a scrollable adapter
        Attendee(name = "Ann Bailey", isCreator = true)
        Attendee(name = "Bertha Cole", isCreator = false)
    }
}

@Composable
fun Attendee(name: String, isCreator: Boolean) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.reminder_gray)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AttendeeProfile(
                initials = "AB" // TODO getInitials from name
            )
            AttendeeName(name, modifier = Modifier.weight(1f))
            if (isCreator) {
                CreatorText()
            } else {
                TrashIcon()
            }
        }
    }
}


@Composable
fun AttendeeProfile(
    initials: String
) {
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            .background(color = colorResource(id = R.color.gray), shape = CircleShape)
            .size(34.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AttendeeName(name: String, modifier: Modifier) {
    Text(
        text = name,
        fontSize = 14.sp,
        color = colorResource(id = R.color.dark_gray),
        modifier = modifier.padding(start = 16.dp)
    )
}

@Composable
fun TrashIcon() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_trash),
            contentDescription = stringResource(id = R.string.cancel),
            tint = colorResource(id = R.color.dark_gray)
        )
    }
}

@Composable
fun CreatorText() {
    Text(
        text = stringResource(R.string.creator),
        fontSize = 14.sp,
        modifier = Modifier.padding(end = 16.dp),
        color = colorResource(id = R.color.light_blue)
    )
}