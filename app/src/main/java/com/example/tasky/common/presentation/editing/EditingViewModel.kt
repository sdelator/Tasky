package com.example.tasky.common.presentation.editing

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EditingViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TAG = "EditingViewModel"
    }

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> get() = _text

    fun save(text: String) {
        Log.d("sandra", "text to save $text")
    }

    fun onTextChange(newText: String) {
        _text.value = newText
    }
}