package com.example.tasky.feature_login.domain.model

import com.example.tasky.common.data.util.DataError

open class AuthenticationViewState {
    object Success : AuthenticationViewState()
    data class Failure(val dataError: DataError) : AuthenticationViewState()
    object Loading : AuthenticationViewState()
}