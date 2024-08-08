package com.example.tasky.common.data.notification_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tasky.R
import com.example.tasky.common.presentation.CardAction

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
            val agendaItemId = inputData.getString("agendaItemId") ?: ""
            val date = inputData.getLong("date", 0)
            val agendaItemType = inputData.getString("agendaItemType") ?: ""

            createAndShowNotification(
                notificationId,
                title,
                description,
                date = date,
                agendaItemId = agendaItemId,
                agendaItemType = agendaItemType
            )
            Result.success()
            return Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }

    private fun createAndShowNotification(
        notificationId: Int,
        title: String,
        message: String,
        date: Long,
        agendaItemId: String,
        agendaItemType: String
    ) {
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
        val cardAction = CardAction.Open.name
        val activityIntent = Intent(Intent.ACTION_VIEW).apply {
            data =
                Uri.parse("taskyapp://details/$date?agendaItemType=$agendaItemType&agendaItemId=$agendaItemId&cardAction=$cardAction")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(title)
            .setContentText(message)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        manager?.notify(notificationId, notification)
    }
}