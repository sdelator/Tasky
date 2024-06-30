package com.example.tasky.common.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val NAME = stringPreferencesKey("name")
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
}