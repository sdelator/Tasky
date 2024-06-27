package com.example.tasky.feature_login.presentation

import com.example.tasky.common.domain.error.DataError

sealed class AuthenticationViewState {
    data class ErrorDialog(val dataError: DataError) : AuthenticationViewState()
    object LoadingSpinner : AuthenticationViewState()
}