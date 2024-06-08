package com.example.tasky

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class TaskyApplication : Application() {
    private companion object {
        const val TAG = "TaskyApplication"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("sandra", TAG)
    }
}