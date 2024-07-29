package com.example.tasky.agenda_details.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.tasky.agenda_details.domain.ImageCompressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ImageCompressorImpl(private val contentResolver: ContentResolver) :
    ImageCompressor {
    override suspend fun compressImage(drawable: Drawable, quality: Int): ByteArray {
        return withContext(Dispatchers.IO) {
            val bitmap = (drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            stream.toByteArray()
        }
    }

    override suspend fun uriStringToDrawable(uri: String): Drawable? {
        return contentResolver.openInputStream(Uri.parse(uri))?.use { inputStream ->
            Drawable.createFromStream(inputStream, uri)
        }
    }
}