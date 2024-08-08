package com.example.tasky.common.data.notification_service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.example.tasky.common.domain.Constants
import com.example.tasky.common.domain.notification.NotificationHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

/**
 * Set up and cancel alarm using AlarmManager
 */
class NotificationHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationHandler {
    companion object {
        private const val TAG = "NotificationHandler"
    }

    override fun cancelAlarmAndNotification(notificationId: String) {
        val uniqueId = uuidToInt(notificationId)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, NotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uniqueId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)

        // cancel ongoing notification
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(uniqueId)
    }

    override fun initNotification(
        notificationId: String,
        title: String,
        description: String,
        remindAt: Long
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Request permission to schedule exact alarms
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return
            }
        }

        scheduleExactAlarm(notificationId, title, description, remindAt)
    }

    private fun scheduleExactAlarm(
        notificationId: String,
        title: String,
        description: String,
        remindAt: Long
    ) {
        val uniqueId = uuidToInt(notificationId)
        val alarmIntent = Intent(context, NotificationBroadcastReceiver::class.java)
        alarmIntent.putExtra(Constants.NOTIFICATION_ID, uniqueId)
        alarmIntent.putExtra("title", title)
        alarmIntent.putExtra("description", description)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            uniqueId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            remindAt,
            pendingIntent
        )
    }

    override fun setNotification() {
        //check api and see if there is a new notification
    }

    override fun updateNotification(
        notificationId: String,
        title: String,
        description: String,
        remindAt: Long
    ) {
        cancelAlarmAndNotification(notificationId)
        initNotification(notificationId, title, description, remindAt)
    }


    fun uuidToInt(uuidString: String): Int {
        val uuid = UUID.fromString(uuidString)
        return uuid.hashCode()
    }
}