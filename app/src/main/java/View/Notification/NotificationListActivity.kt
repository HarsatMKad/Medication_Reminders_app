package View.Notification

import ViewModel.Journal.JournalViewModel
import ViewModel.Notification.NotificationSender
import ViewModel.Notification.NotificationViewModel
import ViewModel.Notification.NotificationWithCureViewModel
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medication_reminders_app.R
import Model.Journal.Journal
import Model.Notification.Notification
import Model.Notification.NotificationWithCure
import View.Cure.CureList
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SerializableNotification(
    val id: Int,
    val hours: Int,
    val minutes: Int,
    val period: Long,
    val cureId: Int): Serializable

class NotificationListActivity : AppCompatActivity(), NotifClickDeleteInterface,
    NotifClickEditInterface, NotifClickAcceptInterface, NotifClickPutoffInterface,
    timeOverInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)

        val createBtn = findViewById<Button>(R.id.buttonAdd)
        val backBtn = findViewById<Button>(R.id.backBtnNotifList)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerNotification)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = NotificationAdapter(this, this, this, this, this, this)
        recyclerView.adapter = adapter

        val viewModelList = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NotificationWithCureViewModel::class.java)
        viewModelList.notificationData.observe(this, Observer { list ->
            list.let{
                adapter.updateList(it)
            }
        })

        backBtn.setOnClickListener {
            val intent = Intent(this, CureList::class.java)
            startActivity(intent)
        }

        createBtn.setOnClickListener{
            val intent = Intent(this, NotificationCreate::class.java)
            startActivity(intent)
        }
    }

    override fun onDeleteClick(notification: Notification) {
        val viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NotificationViewModel::class.java)
        viewModel.deleteNotification(notification)
        val sender = NotificationSender(baseContext)
        val tag = notification.id.toString()
        sender.delSendByTag(tag)
        Toast.makeText(this, "Удалено", Toast.LENGTH_SHORT).show()
    }
    override fun onEditClick(notification: NotificationWithCure) {

        val notif = SerializableNotification(notification.notification.id,
            notification.notification.hours,
            notification.notification.minutes,
            notification.notification.period,
            notification.notification.cureId.toInt())

        val intent = Intent(this, EditNotificationActivity::class.java)

        intent.putExtra("notification", notif)
        startActivity(intent)
        this.finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAcceptClick(notification: NotificationWithCure) {
        val journalViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            JournalViewModel::class.java)

        val curentDate = LocalDateTime.now().plusHours(7)
        val dtfCustom = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val dateString = curentDate.format(dtfCustom)

        val journalUnit = Journal(notification.cure.title, notification.cure.dosage, dateString, true)
        journalViewModel.addJournal(journalUnit)

        val viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NotificationViewModel::class.java)

        val newNotification = notification.notification
        newNotification.id = notification.notification.id
        newNotification.days += 1

        viewModel.updateNotification(newNotification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPutoffClick(notification: Notification) {
        val viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NotificationViewModel::class.java)

        val newNotification = notification
        newNotification.id = notification.id
        newNotification.minutes += 20

        viewModel.updateNotification(newNotification)

        val sender = NotificationSender(baseContext)
        sender.delSendByTag(notification.id.toString())
        sender.addSend(newNotification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun timeOverForNotification(notification: NotificationWithCure) {
        val journalViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            JournalViewModel::class.java)
        val notificationViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NotificationViewModel::class.java)

        val curentDate = LocalDateTime.now().plusHours(7)
        val dtfCustom = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val dateString = curentDate.format(dtfCustom)

        val journalUnit = Journal(notification.cure.title, notification.cure.dosage, dateString, false)
        journalViewModel.addJournal(journalUnit)

        val newNotification = notification.notification
        newNotification.id = notification.notification.id
        newNotification.days += 1

        notificationViewModel.updateNotification(newNotification)
    }
}