package com.example.tasky.agenda_details.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import com.example.tasky.agenda_details.domain.ImageCompressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ImageCompressorImpl(private val contentResolver: ContentResolver) :
    ImageCompressor {
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

    override fun byteArrayToString(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun stringToByteArray(base64String: String): ByteArray {
        return Base64.decode(base64String, Base64.DEFAULT)
    }
}