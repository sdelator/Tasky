package com.example.tasky.feature_login.domain.model

data class RegisterUserInfo(
    val fullName: String,
    val email: String,
    val password: String
)

data class LoginUserInfo(
    val email: String,
    val password: String
)

data class LoginUserResponse(
    val accessToken: String,
    val refreshToken: String,
    val fullName: String,
    val userId: String,
    val accessTokenExpirationTimestamp: Long
)

data class AccessToken(
    val refreshToken: String,
    val userId: String
)

data class AccessTokenResponse(
    val accessToken: String,
    val expirationTimestamp: Long
)