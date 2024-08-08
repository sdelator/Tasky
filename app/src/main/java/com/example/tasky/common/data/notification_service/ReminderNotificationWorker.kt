package com.example.tasky.common.data.notification_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tasky.MainActivity
import com.example.tasky.R

/**
 * Handles the actual work of creating and displaying the notification
 */
class ReminderNotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    companion object {
        private const val TAG = "ReminderNotificationWorker"
        const val REMINDER_CHANNEL_ID = "reminder_channel"
    }

    override fun doWork(): Result {
        return try {
            val notificationId = inputData.getInt("notificationId", 0)
            val title = inputData.getString("title") ?: ""
            val description = inputData.getString("description") ?: ""
            createAndShowNotification(notificationId, title, description)
            Result.success()
            return Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }

    private fun createAndShowNotification(notificationId: Int, title: String, message: String) {
        val context = applicationContext

        val channel = NotificationChannel(
            REMINDER_CHANNEL_ID,
            "Reminder",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Used for event reminders"
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
        //todo add deepLinkNavigationData
        val activityIntent =
            Intent(context, MainActivity::class.java) //todo navigate to specific screen
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(title)
            .setContentText(message)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setContentIntent(pendingIntent)
            .build()

        manager?.notify(notificationId, notification)
    }
}