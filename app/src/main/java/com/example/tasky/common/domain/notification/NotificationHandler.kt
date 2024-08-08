package com.example.tasky.common.domain.notification

interface NotificationHandler {
    fun cancelAlarmAndNotification(notificationId: String)
    fun initNotification(
        notificationId: String,
        title: String,
        description: String,
        remindAt: Long,
        agendaItemId: String,
        date: Long,
        agendaItemType: String
    )

    fun setNotification()
    fun updateNotification(
        notificationId: String,
        title: String,
        description: String,
        remindAt: Long,
        agendaItemId: String,
        date: Long,
        agendaItemType: String
    )
}