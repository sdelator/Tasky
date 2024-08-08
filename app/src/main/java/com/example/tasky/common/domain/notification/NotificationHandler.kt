package com.example.tasky.common.domain.notification

interface NotificationHandler {
    fun cancelAlarmAndNotification(notificationId: String)
    fun initNotification(notificationId: String, title: String, description: String, remindAt: Long)
    fun setNotification()
}