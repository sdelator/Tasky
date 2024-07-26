package com.example.tasky.agenda_details.domain

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri

interface ImageCompressionUseCase {
    suspend fun compressImage(drawable: Drawable): ByteArray
    suspend fun uriToDrawable(context: Context, uri: Uri): Drawable?

}