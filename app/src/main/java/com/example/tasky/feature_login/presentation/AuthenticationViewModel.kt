package com.example.tasky.feature_login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.domain.util.Result
import com.example.tasky.feature_login.domain.model.AuthenticationViewState
import com.example.tasky.feature_login.domain.model.LoginUserInfo
import com.example.tasky.feature_login.domain.model.RegisterUserInfo
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) : ViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
    }

    // viewState triggered by API response
    private val _viewState =
        MutableStateFlow<AuthenticationViewState?>(null)
    val viewState: StateFlow<AuthenticationViewState?>
        get() = _viewState


    fun registerUserClicked(userInfo: RegisterUserInfo) {
        Log.d(TAG, "registerUserClicked and userInfo is $userInfo")
        viewModelScope.launch {
            _viewState.emit(AuthenticationViewState.Loading)
            val result = userRemoteRepository.postRegisterCall(userInfo)

            when (result) {
                is Result.Success -> {
                    println("success register!")
                    // emit a viewState to show LoginScreen composable
                    _viewState.emit(AuthenticationViewState.Success)
                }

                is Result.Error -> {
                    println("failed register :(")
                    // emit a viewState to show ErrorMessage
                    val errorMsg = result.error.formatErrorMessage()
                    _viewState.emit(AuthenticationViewState.Failure(errorMsg))
                }
            }
        }
    }

    fun logInClicked(userCredentials: LoginUserInfo) {
        Log.d(TAG, "logInClicked and userCredentials is $userCredentials")
        viewModelScope.launch {
            _viewState.emit(AuthenticationViewState.Loading)
            val result = userRemoteRepository.postLoginCall(userCredentials)

            when (result) {
                is Result.Success -> {
                    println("success login!")
                    // emit a viewState to change to agenda composable
                    _viewState.emit(AuthenticationViewState.Success)
                }

                is Result.Error -> {
                    println("failed login :(")
                    // emit a viewState to show ErrorMessage
                    val errorMsg = result.error.formatErrorMessage()
                    _viewState.emit(AuthenticationViewState.Failure(errorMsg))
                }
            }
        }
    }
}