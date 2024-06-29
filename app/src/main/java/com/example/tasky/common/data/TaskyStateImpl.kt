package com.example.tasky.common.data

import com.example.tasky.common.domain.TaskyState
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskyStateImpl @Inject constructor(
    private val appPreferences: PreferenceHelper,
) : TaskyState {
    companion object {
        private const val TAG = "TaskyState"
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