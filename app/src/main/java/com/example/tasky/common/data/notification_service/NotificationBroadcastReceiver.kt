package com.example.tasky.common.data.notification_service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.tasky.common.domain.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Receive the broadcast from AlarmManager and delegate the work to NotificationWorker
 */
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
        val agendaItemId = intent.getStringExtra("agendaItemId") ?: ""
        val date = intent.getLongExtra("date", 0)
        val agendaItemType = intent.getStringExtra("agendaItemType") ?: ""

        val workManager = WorkManager.getInstance(context)

        val data = Data.Builder()
            .putInt("notificationId", notificationId)
            .putString("title", title)
            .putString("description", description)
            .putString("agendaItemId", agendaItemId)
            .putLong("date", date)
            .putString("agendaItemType", agendaItemType)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(ReminderNotificationWorker::class.java)
            .setInputData(data)
            .build()

        workManager.enqueue(workRequest)
    }
}