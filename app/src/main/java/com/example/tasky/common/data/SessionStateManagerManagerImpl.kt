package com.example.tasky.common.data

import com.example.tasky.common.domain.SessionStateManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStateManagerManagerImpl @Inject constructor(
    private val appPreferences: PreferenceHelper,
) : SessionStateManager {
    companion object {
        private const val TAG = "SessionStateManager"
    }

    override suspend fun setAccessToken(accessToken: String) {
        appPreferences.put(PreferencesKeys.ACCESS_TOKEN, accessToken)
    }

    override suspend fun getAccessToken(): String {
        return appPreferences.get(PreferencesKeys.ACCESS_TOKEN).first() ?: ""
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        appPreferences.put(PreferencesKeys.REFRESH_TOKEN, refreshToken)
    }

    override suspend fun getRefreshToken(): String {
        return appPreferences.get(PreferencesKeys.REFRESH_TOKEN).first() ?: ""
    }

    override suspend fun setName(name: String) {
        appPreferences.put(PreferencesKeys.NAME, name)
    }

    override suspend fun getName(): String {
        return appPreferences.get(PreferencesKeys.NAME).first() ?: ""
    }


}