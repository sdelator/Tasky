package com.example.tasky.agenda_details.data

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object MimeUtils {
    fun getMimeType(contentResolver: ContentResolver, uri: String): String? {
        val type = contentResolver.getType(Uri.parse(uri))
        return type
    }

    fun getExtensionFromMimeType(mimeType: String?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
    }

    fun toRequestBody(byteArray: ByteArray, mimeType: String?): RequestBody {
        return byteArray.toRequestBody(mimeType?.toMediaTypeOrNull())
    }
}