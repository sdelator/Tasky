package com.example.tasky.feature_login.data.model

data class RegisterUserInfo(
    val fullName: String,
    val email: String,
    val password: String
)

data class LoginUserInfo(
    val email: String,
    val password: String
)

data class AccessToken(
    val refreshToken: String,
    val userId: String
)

data class AccessTokenResponse(
    val accessToken: String,
    val expirationTimestamp: Long
)