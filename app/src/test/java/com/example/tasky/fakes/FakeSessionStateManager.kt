package com.example.tasky.fakes

import com.example.tasky.common.domain.SessionStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class FakeSessionStateManager : SessionStateManager {
    private val refreshTokenFlow = MutableStateFlow<String?>(null)
    private val nameFlow = MutableStateFlow<String?>(null)

    override suspend fun setRefreshToken(refreshToken: String) {
        refreshTokenFlow.value = refreshToken
    }

    override suspend fun getRefreshToken(): String {
        return refreshTokenFlow.first() ?: ""
    }

    override suspend fun setName(name: String) {
        nameFlow.value = name
    }

    override suspend fun getName(): String {
        return nameFlow.first() ?: ""
    }
}

