package ViewModel.Notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import Model.Notification.Notification
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import java.util.concurrent.TimeUnit

class NotificationSender(context: Context) {
    private val CHANNEL_ID = "channel_id_example_01"
    val c = context
    init {
        createNotificationChannel()
    }

    //Создает повторяющееся уведомление
    @RequiresApi(Build.VERSION_CODES.O)
    fun addSend(notification: Notification) {
        val data = Data.Builder()
        data.putString("name", notification.header)
        data.putString("text", notification.shortDescription)
        data.putString("mainText", notification.description)

        val dnow = LocalDateTime.now().plusHours(7)
        var dateNow = LocalDate.now()
        if(notification.hours <= 7){
            dateNow = LocalDate.now().plusDays(1)
        }
        var dlate = LocalDateTime.of(dateNow, LocalTime.of(notification.hours, notification.minutes))
        var difference = Duration.between(dnow, dlate).seconds/60

        if(difference < 0){
            dlate = LocalDateTime.of(dateNow, LocalTime.of(15, 0)).plusDays(1)
            difference = Duration.between(dnow, dlate).seconds/60
        }

        Toast.makeText(c, "Сработает через: " + difference.toString() + " минут", Toast.LENGTH_SHORT).show()

        val tag = notification.id.toString()
        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            notification.period, TimeUnit.DAYS)
            .setInputData(data.build())
            .setInitialDelay(difference, TimeUnit.MINUTES)
            .addTag(tag)
            .build()

        WorkManager.getInstance(c).enqueue(notificationWorkRequest)
    }

    //удаляет все повторяющиеся события(уведомления)
    fun delAllSend(){
        WorkManager.getInstance(c).cancelAllWork()
    }

    //удаляет повторяющиеся события(уведомления) от его тегу
    fun delSendByTag(tag: String){
        WorkManager.getInstance(c).cancelAllWorkByTag(tag)
    }

    //удаляет повторяющиеся события(уведомления) по его id
    fun delSendById(id: UUID){
        WorkManager.getInstance(c).cancelWorkById(id)
    }

    //создает канал для уведомлений
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "channelName"
            val descriptionText = "Notivifaction Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(c, NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
