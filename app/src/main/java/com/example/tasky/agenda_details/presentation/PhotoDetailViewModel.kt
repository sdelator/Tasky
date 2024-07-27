package com.example.tasky.agenda_details.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tasky.agenda_details.domain.ImageCompressor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val imageCompressor: ImageCompressor
) : ViewModel() {
    companion object {
        const val TAG = "PhotoDetailViewModel"
    }

    suspend fun convertStringToImage(byteString: String): ByteArray {
        return imageCompressor.stringToByteArray(byteString)
    }

    fun erasePhoto() {
        Log.d("sandra", "erase photo")
    }
}