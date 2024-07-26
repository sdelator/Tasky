package com.example.tasky.agenda_details.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import java.io.ByteArrayOutputStream

class ImageCompressionUseCase {
    fun compressImage(drawable: Drawable, quality: Int = 100): ByteArray {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    fun uriToDrawable(context: Context, uri: Uri): Drawable? {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        return Drawable.createFromStream(inputStream, uri.toString())
    }
}