package com.example.tasky.common.presentation.notification_service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.tasky.common.domain.Constants
import com.example.tasky.common.domain.notification.NotificationHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

class NotificationHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationHandler {
    companion object {
        private const val TAG = "NotificationHandler"
    }

    override fun cancelAlarmAndNotification(notificationId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)

        // cancel ongoing notification
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    override fun initNotification(notificationId: Int) {
        val alarmIntent = Intent(context, NotificationBroadcastReceiver::class.java)
        alarmIntent.putExtra(Constants.NOTIFICATION_ID, notificationId)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            Date().toInstant().epochSecond,
            10000,
            pendingIntent
        )
    }

    override fun setNotification() {
        //check api and see if there is a new notification
    }
}