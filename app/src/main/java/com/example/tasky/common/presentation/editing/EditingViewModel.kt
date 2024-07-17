package com.example.tasky.common.presentation.editing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.util.isValidName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EditingViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val TAG = "EditingViewModel"
    }

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> get() = _text

    val isTitleValid = _text.map { text ->
        text.isValidName()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)


    fun formatTitle(text: String): String {
        return text.trim()
    }

    fun onTextChange(newText: String) {
        _text.value = newText
    }
}