package com.example.tasky.fakes

import com.example.tasky.common.domain.SessionStateManager
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSessionStateManager : SessionStateManager {
    private val refreshTokenFlow = MutableStateFlow<String?>(null)
    private val nameFlow = MutableStateFlow<String?>(null)

    override suspend fun setRefreshToken(refreshToken: String) {
        refreshTokenFlow.value = refreshToken
    }

    override fun getRefreshToken(): String? {
        return refreshTokenFlow.value
    }

    override suspend fun setName(name: String) {
        nameFlow.value = name
    }

    override fun getName(): String? {
        return nameFlow.value
    }
}

