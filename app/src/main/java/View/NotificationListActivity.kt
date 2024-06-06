package View

import ViewModel.CureViewModel
import ViewModel.NotificationSender
import ViewModel.NotificationViewModel
import ViewModel.NotificationWithCureViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.medication_reminders_app.R
import com.example.medication_reminders_app.data.Cure
import com.example.medication_reminders_app.data.CureRepository
import com.example.medication_reminders_app.data.Notification
import com.example.medication_reminders_app.data.NotificationRepository
import com.example.medication_reminders_app.data.NotificationWithCure
import com.example.medication_reminders_app.data.RemindersDatabase
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class SerializableNotification(
    val id: Int,
    val hours: Int,
    val minutes: Int,
    val period: Long,
    val cureId: Int): Serializable

class NotificationListActivity : AppCompatActivity(), NotifClickDeleteInterface,
    NotifClickEditInterface, NotifClickAcceptInterface, NotifClickPutoffInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)

        val createBtn = findViewById<Button>(R.id.buttonAdd)
        val backBtn = findViewById<Button>(R.id.backBtnNotifList)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerNotification)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = NotificationAdapter(this, this, this, this, this)
        recyclerView.adapter = adapter

        val viewModelList = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NotificationWithCureViewModel::class.java)
        viewModelList.notificationData.observe(this, Observer { list ->
            list.let{
                adapter.updateList(it)
            }
        })

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        createBtn.setOnClickListener{
            val intent = Intent(this, NotificationCreate::class.java)

            startActivity(intent)
            this.finish()
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

    override fun onAcceptClick(notification: NotificationWithCure) {
        Log.v("accept", "accept")
    }

    override fun onPutoffClick(notification: Notification) {
        Log.v("putoff", "putoff")
    }
}