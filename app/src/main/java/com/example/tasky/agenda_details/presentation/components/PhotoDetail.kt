package com.example.tasky.agenda_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tasky.R
import com.example.tasky.agenda_details.presentation.PhotoDetailViewModel
import kotlinx.coroutines.coroutineScope

@Composable
fun PhotoDetailRoot(
    navController: NavController,
    image: String,
    photoPhotoDetailViewModel: PhotoDetailViewModel = hiltViewModel()
) {
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }
    LaunchedEffect(image) {
        coroutineScope {
            imageByteArray = photoPhotoDetailViewModel.convertStringToImage(image)
        }
    }

    imageByteArray?.let {
        PhotoDetailContent(
            image = it,
            onClick = {
                when (it) {
                    PhotoDetailToolbarAction.Cancel -> navController.popBackStack()
                    PhotoDetailToolbarAction.Erase -> photoPhotoDetailViewModel.erasePhoto()
                }
            }
        )
    }
}

@Composable
fun PhotoDetailContent(
    image: ByteArray,
    onClick: (PhotoDetailToolbarAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {
        Column {
            PhotoDetailToolbar(onClick = onClick)
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 50.dp, bottom = 100.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .border(2.dp, colorResource(id = R.color.light_blue)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun PhotoDetailToolbar(
    onClick: (PhotoDetailToolbarAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CancelButton(onClick = onClick)
        Text(
            text = stringResource(id = R.string.photo),
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        TrashButton(onClick = onClick)
    }
}

@Composable
fun CancelButton(onClick: (PhotoDetailToolbarAction) -> Unit) {
    IconButton(onClick = { onClick(PhotoDetailToolbarAction.Cancel) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(id = R.string.cancel),
            tint = Color.White
        )
    }
}

@Composable
fun TrashButton(onClick: (PhotoDetailToolbarAction) -> Unit) {
    IconButton(onClick = { onClick(PhotoDetailToolbarAction.Erase) }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_trash),
            contentDescription = "Delete",
            tint = Color.White
        )
    }
}

@Composable
@Preview
fun PreviewPhotoDetailsRootComposable() {
    PhotoDetailContent(
        image = ByteArray(123),
        onClick = {}
    )
}

enum class PhotoDetailToolbarAction {
    Cancel, Erase
}
