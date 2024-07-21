package com.example.tasky.agenda_details.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

fun Uri.toDrawable(context: Context): Drawable? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(this)
    return Drawable.createFromStream(inputStream, this.toString())
}

fun Context.reduceImageSize(drawable: Drawable?): Bitmap? {
    if (drawable == null) {
        return null
    }
    val baos = ByteArrayOutputStream()
    val bitmap = drawable.toBitmap(200, 200, Bitmap.Config.ARGB_8888)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageBytes = baos.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}