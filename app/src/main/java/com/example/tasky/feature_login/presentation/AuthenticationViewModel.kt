package com.example.tasky.feature_login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.model.Resource
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
            val resource = userRemoteRepository.postRegisterCall(userInfo)

            when (resource) {
                is Resource.Success -> {
                    println("success register!")
                    // emit a viewState to show LoginScreen composable
                    _viewState.emit(AuthenticationViewState.Success)
                }

                is Resource.Failure -> {
                    println("failed register :(")
                    // emit a viewState to show ErrorMessage
                    _viewState.emit(AuthenticationViewState.Failure(resource.errorMessage.toString()))
                }
            }
        }
    }

    fun logInClicked(userCredentials: LoginUserInfo) {
        Log.d(TAG, "logInClicked and userCredentials is $userCredentials")
        viewModelScope.launch {
            _viewState.emit(AuthenticationViewState.Loading)
            val resource = userRemoteRepository.postLoginCall(userCredentials)

            when (resource) {
                is Resource.Success -> {
                    println("success login!")
                    // emit a viewState to change to calendar composable
                    _viewState.emit(AuthenticationViewState.Success)
                }

                is Resource.Failure -> {
                    println("failed login :(")
                    // emit a viewState to show ErrorMessage
                    _viewState.emit(AuthenticationViewState.Failure(resource.errorMessage.toString()))
                }
            }
        }
    }
}