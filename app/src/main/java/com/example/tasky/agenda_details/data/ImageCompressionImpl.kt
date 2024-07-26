package com.example.tasky.agenda_details.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.tasky.agenda_details.domain.ImageCompressionUseCase
import java.io.ByteArrayOutputStream

class ImageCompressionUseCaseImpl : ImageCompressionUseCase {
    override fun compressImage(drawable: Drawable): ByteArray {
        val quality = 100
        val bitmap = (drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    override fun uriToDrawable(context: Context, uri: Uri): Drawable? {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        return Drawable.createFromStream(inputStream, uri.toString())
    }
}