package com.example.tasky.feature_login.data.remote

import com.example.tasky.feature_login.data.model.AccessToken
import com.example.tasky.feature_login.data.model.AccessTokenResponse
import com.example.tasky.feature_login.data.model.LoginUserInfo
import com.example.tasky.feature_login.data.model.RegisterUserInfo
import com.example.tasky.feature_login.domain.model.LoginUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TaskyApi {
    @POST("/register")
    suspend fun registerUser(
        @Body registerUserInfo: RegisterUserInfo
    ): Response<Unit>

    @POST("/login")
    suspend fun loginUser(
        @Body loginUserInfo: LoginUserInfo
    ): Response<LoginUserResponse>

    @POST("/accessToken")
    suspend fun getNewAccessToken(@Body accessToken: AccessToken): AccessTokenResponse

    @GET("/authenticate")
    suspend fun checkAuthentication()

    @GET("/logout")
    suspend fun logout()
}