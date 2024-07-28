package com.example.tasky.agenda_details.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.tasky.agenda_details.domain.ImageCompressor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val imageCompressor: ImageCompressor
) : ViewModel() {
    companion object {
        const val TAG = "PhotoDetailViewModel"
    }

    private val _viewState = MutableStateFlow(AgendaDetailsViewState())
    val viewState: StateFlow<AgendaDetailsViewState> = _viewState

    fun convertStringToUri(uriString: String): Uri {
        return Uri.parse(uriString)
    }
}