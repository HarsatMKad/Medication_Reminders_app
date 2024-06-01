package ViewModel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LifecycleOwner
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.time.TimeSource

class NotificationSender(context: Context) {
    private val CHANNEL_ID = "channel_id_example_01"
    val c = context
    init {
        createNotificationChannel()
    }

    //Создает повторяющееся уведомление
    @RequiresApi(Build.VERSION_CODES.O)
    fun addSend(title: String, desctiption: String, mainText: String, hours: Int, minuts: Int, periodHours: Long, tag: String) {
        val data = Data.Builder()
        data.putString("name", title)
        data.putString("text", desctiption)
        data.putString("mainText", mainText)

        val dnow = LocalDateTime.now().plusHours(7)
        val dateNow = LocalDate.now()
        var dlate = LocalDateTime.of(dateNow, LocalTime.of(hours, minuts))

        var difference = Duration.between(dnow, dlate).seconds/60

        if(difference < 0){
            dlate = LocalDateTime.of(dateNow, LocalTime.of(15, 0)).plusDays(1)
            difference = Duration.between(dnow, dlate).seconds/60
        }

        Log.v("delay", "сработает через: " + difference.toString() + " минут")

        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            periodHours, TimeUnit.DAYS)
            .setInputData(data.build())
            .setInitialDelay(difference, TimeUnit.DAYS)
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

    //выводит в логи данные о текущих повторяющихся событиях (криво, но работает)
    fun logSendsTest(tag: String){
        WorkManager.getInstance(c).getWorkInfosByTagLiveData(tag)
            .observe(c as LifecycleOwner, {
                Log.v("1", "Начало")
                for(i in 0..it.size-1){
                    Log.d(it[i].tags.toString(),  "onChanged: " + it[i].state + " id: " + it[i].id.toString() + " delay(mills): " + it[i].initialDelayMillis);
                }
                Log.v("1", "Конец")
            })
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
