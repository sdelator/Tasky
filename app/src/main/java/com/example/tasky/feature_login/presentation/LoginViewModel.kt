package com.example.tasky.feature_login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.feature_login.domain.model.LoginUserInfo
import com.example.tasky.feature_login.domain.model.RegisterUserInfo
import com.example.tasky.feature_login.domain.repository.UserRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRemoteRepository: UserRemoteRepository
) : ViewModel() {

    companion object {
        const val TAG = "LoginViewModel"
    }

    fun registerUserClicked(userInfo: RegisterUserInfo) {
        Log.d(TAG, "registerUserClicked and userInfo is $userInfo")
        viewModelScope.launch {
            userRemoteRepository.postRegisterCall(userInfo)
        }
    }

    fun logInClicked(userCredentials: LoginUserInfo) {
        Log.d(TAG, "logInClicked and userCredentials is $userCredentials")
        viewModelScope.launch {
            userRemoteRepository.postLoginCall(userCredentials)
        }
    }
}