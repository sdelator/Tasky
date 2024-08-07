package com.example.tasky.common.domain.notification

interface NotificationHandler {
    fun cancelAlarmAndNotification(notificationId: Int)
    fun initNotification(notificationId: Int)
    fun setNotification()
}