package com.example.tasky.common.presentation

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.tasky.MainActivity
import com.example.tasky.R

class ReminderNotificationService(
    private val context: Context
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(reminder: String = "") {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle("Reminder")
            .setContentText("go to store")
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setContentIntent(activityPendingIntent)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = (System.currentTimeMillis() % Integer.MAX_VALUE).toInt()
        notificationManager.notify(notificationId, notification)
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "reminder_channel"
    }
}