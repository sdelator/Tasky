package com.example.tasky.feature_login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.common.model.Resource
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
            val resource = userRemoteRepository.postRegisterCall(userInfo)

            when (resource) {
                is Resource.Success -> {
                    println("success register!")
                    // emit a viewState to show LoginScreen composable
                }

                is Resource.Failure -> {
                    println("failed register :(")
                    // emit a viewState to show ErrorMessage
                }
            }
        }
    }

    fun logInClicked(userCredentials: LoginUserInfo) {
        Log.d(TAG, "logInClicked and userCredentials is $userCredentials")
        viewModelScope.launch {
            val resource = userRemoteRepository.postLoginCall(userCredentials)

            when (resource) {
                is Resource.Success -> {
                    println("success login!")
                    //emit a viewState to change to calendar composable
                }

                is Resource.Failure -> {
                    println("failed login :(")
                    //emit a viewState to show ErrorMessage
                }
            }
        }
    }
}