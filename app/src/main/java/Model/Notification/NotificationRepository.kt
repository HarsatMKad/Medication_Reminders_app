package com.example.medication_reminders_app.data.Notification

import androidx.lifecycle.LiveData

class NotificationRepository(private val notificationDao: NotificationsDao) {
    val allNotifications: LiveData<List<Notification>> = notificationDao.getAllNotifications()
    val notificationWithCure: LiveData<List<NotificationWithCure>> = notificationDao.getNotificationWithCure()

    suspend fun insert(notification: Notification) {
        notificationDao.insert(notification)
    }

    suspend fun delete(notification: Notification){
        notificationDao.delete(notification)
    }

    suspend fun update(notification: Notification){
        notificationDao.update(notification)
    }
}