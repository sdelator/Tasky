package com.example.tasky.common.data

import com.example.tasky.common.domain.SessionStateManager
import kotlinx.coroutines.flow.Flow
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

    override fun getAccessToken(): Flow<String?> {
        return appPreferences.get(PreferencesKeys.ACCESS_TOKEN)
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        appPreferences.put(PreferencesKeys.REFRESH_TOKEN, refreshToken)
    }

    override fun getRefreshToken(): String? {
        return runBlocking {
            appPreferences.get(PreferencesKeys.REFRESH_TOKEN).firstOrNull()
        }
    }

    override suspend fun setName(name: String) {
        appPreferences.put(PreferencesKeys.NAME, name)
    }

    override fun getName(): String? {
        return runBlocking {
            appPreferences.get(PreferencesKeys.NAME).firstOrNull()
        }
    }


}