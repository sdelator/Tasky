package com.example.tasky.agenda_details.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.tasky.agenda_details.domain.ImageCompressionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ImageCompressionUseCaseImpl(private val contentResolver: ContentResolver) :
    ImageCompressionUseCase {
    override suspend fun compressImage(drawable: Drawable): ByteArray {
        return withContext(Dispatchers.IO) {
            val quality = 80
            val bitmap = (drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            stream.toByteArray()
        }
    }

    override suspend fun uriToDrawable(uri: Uri): Drawable? {
        return withContext(Dispatchers.IO) {
            val inputStream = contentResolver.openInputStream(uri)
            Drawable.createFromStream(inputStream, uri.toString())
        }
    }
}