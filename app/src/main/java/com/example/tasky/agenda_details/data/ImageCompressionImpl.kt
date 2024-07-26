package com.example.tasky.agenda_details.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.tasky.agenda_details.domain.ImageCompressionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ImageCompressionUseCaseImpl : ImageCompressionUseCase {
    override suspend fun compressImage(drawable: Drawable): ByteArray {
        return withContext(Dispatchers.IO) {
            val quality = 100
            val bitmap = (drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            stream.toByteArray()
        }
    }

    override suspend fun uriToDrawable(context: Context, uri: Uri): Drawable? {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            Drawable.createFromStream(inputStream, uri.toString())
        }
    }
}