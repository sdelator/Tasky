package com.example.tasky.agenda_details.domain

import android.graphics.drawable.Drawable
import android.net.Uri

interface ImageCompressor {
    suspend fun compressImage(drawable: Drawable): ByteArray
    suspend fun uriToDrawable(uri: Uri): Drawable?

}