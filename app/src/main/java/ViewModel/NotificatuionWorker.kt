package ViewModel

import View.MainActivity
import View.NotificationListActivity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.medication_reminders_app.R
import kotlin.random.Random

class NotificationWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    private val con: Context = appContext
    private val CHANNEL_ID = "channel_id_example_01"
    private val notification_Id = (0..100).random()

    //описывает логику повторяющегося события
    override fun doWork(): Result {
        val name = inputData.getString("name")
        val text = inputData.getString("text")
        val mainText = inputData.getString("mainText")

        sendNotification(name!!, text!!, mainText!!)
        return Result.success()
    }

    //для отправки оповещения
    private fun sendNotification(title: String, text: String, mainText: String) {
        val intent = Intent(con, NotificationListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivities(
            con, 0, arrayOf(intent),
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(con, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(mainText))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager = NotificationManagerCompat.from(con)
        notificationManager.notify(notification_Id, builder)
    }
}
