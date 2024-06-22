package com.example.tasky.feature_login.presentation

import com.example.tasky.common.domain.error.DataError

sealed class AuthenticationViewState {
    object Success : AuthenticationViewState()
    data class Failure(val dataError: DataError) : AuthenticationViewState()
    object Loading : AuthenticationViewState()
}