package com.example.tasky.common.domain.model

data class AccessTokenResponse(
    val accessToken: String,
    val expirationTimestamp: Long
)