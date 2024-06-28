package com.example.tasky.feature_login.presentation

import com.example.tasky.common.domain.error.DataError

sealed class RegisterViewState {
    data class ErrorDialog(val dataError: DataError) : RegisterViewState()
    object LoadingSpinner : RegisterViewState()
}