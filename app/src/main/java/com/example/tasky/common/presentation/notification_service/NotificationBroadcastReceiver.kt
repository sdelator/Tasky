package com.example.tasky.common.presentation.notification_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.tasky.common.domain.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationBroadcastReceiver @Inject constructor() : BroadcastReceiver() {
    companion object {
        const val TAG = "NotificationBroadcastReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        println("onReceive NotificationBroadcastReceiver")
        val notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, 0)
        val title = intent.getStringExtra("title") ?: ""
        val description = intent.getStringExtra("description") ?: ""

        val workManager = WorkManager.getInstance(context)

        val data = Data.Builder()
            .putInt("notificationId", notificationId)
            .putString("title", title)
            .putString("description", description)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(ReminderNotificationWorker::class.java)
            .setInputData(data)
            .build()

        workManager.enqueue(workRequest)
    }
}