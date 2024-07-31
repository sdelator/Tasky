package com.example.tasky.agenda_details.domain

import android.graphics.drawable.Drawable

interface ImageCompressor {
    suspend fun compressImage(drawable: Drawable, quality: Int): ByteArray
    suspend fun uriStringToDrawable(uri: String): Drawable?
}