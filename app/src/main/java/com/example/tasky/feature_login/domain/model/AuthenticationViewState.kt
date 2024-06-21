package com.example.tasky.feature_login.domain.model

open class AuthenticationViewState {
    object Success : AuthenticationViewState()
    data class Failure(val message: String) : AuthenticationViewState()
    object Loading : AuthenticationViewState()
}