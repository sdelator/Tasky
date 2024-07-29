package com.example.tasky.agenda_details.presentation.components

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tasky.R
import com.example.tasky.common.presentation.HeaderMedium

@Composable
fun EmptyPhotos(onAddPhotosClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(top = 30.dp, bottom = 12.dp)
            .clickable { onAddPhotosClick() }
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
                    painter = painterResource(id = R.drawable.ic_add),
                    modifier = Modifier.size(15.dp),
                    tint = colorResource(id = R.color.gray),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    text = stringResource(R.string.add_photos),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.gray)
                )
            }
        }
    }
}

@Composable
fun Photos(
    uriImages: List<Uri?>,
    photoSkipCount: Int,
    onAddPhotosClick: () -> Unit,
    onPhotoClick: (Uri) -> Unit,
    resetPhotoSkipCount: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(photoSkipCount) {
        if (photoSkipCount > 0) {
            Toast.makeText(
                context,
                context.getString(R.string.photos_were_skipped, photoSkipCount),
                Toast.LENGTH_SHORT
            ).show()

            resetPhotoSkipCount()
        }
    }

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
                HeaderMedium(title = stringResource(R.string.photos), textColor = Color.Black)
                Spacer(modifier = Modifier.padding(10.dp))
                LazyRow {
                    items(uriImages) { uri ->
                        uri?.let {
                            AsyncImage(
                                model = it,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .border(2.dp, colorResource(id = R.color.light_blue))
                                    .clickable { onPhotoClick(it) },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    item {
                        if (uriImages.size < 10) {
                            PhotoSlot(onAddPhotosClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoSlot(onAddPhotosClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(2.dp, colorResource(id = R.color.light_blue)),
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clickable(onClick = onAddPhotosClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                stringResource(R.string.add_photos),
                tint = colorResource(id = R.color.light_blue)
            )
        }
    }
}