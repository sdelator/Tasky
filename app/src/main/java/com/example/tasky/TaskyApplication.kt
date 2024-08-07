package com.example.tasky

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.example.tasky.common.presentation.ReminderNotificationService
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class TaskyApplication : Application() {
    private companion object {
        const val TAG = "TaskyApplication"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "OnCreate is called")
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            ReminderNotificationService.REMINDER_CHANNEL_ID,
            "Reminder",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used for event reminders"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}