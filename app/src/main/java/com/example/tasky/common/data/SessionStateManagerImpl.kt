package com.example.tasky.common.data

import com.example.tasky.common.domain.SessionStateManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStateManagerImpl @Inject constructor(
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

    override suspend fun setAccessTokenExpiration(accessTokenExpiration: Long) {
        appPreferences.put(PreferencesKeys.ACCESS_TOKEN_EXPIRATION, accessTokenExpiration)
    }

    override fun getAccessTokenExpiration(): Flow<Long?> {
        return appPreferences.get(PreferencesKeys.ACCESS_TOKEN_EXPIRATION)
    }

    override suspend fun setUserId(userId: String) {
        appPreferences.put(PreferencesKeys.USER_ID, userId)
    }

    override fun getUserId(): Flow<String?> {
        return appPreferences.get(PreferencesKeys.USER_ID)
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