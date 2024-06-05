package View

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.medication_reminders_app.R
import com.example.medication_reminders_app.data.Notification
import com.example.medication_reminders_app.data.NotificationWithCure
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val READY_TYPE = 1
val WAITING_TYPE = 2
class NotificationAdapter(val context: Context,
                          val notifEdit: NotifClickEditInterface,
                          val notifDel: NotifClickDeleteInterface): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    private val notifications = ArrayList<NotificationWithCure>()
    class readyNotifHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText = itemView.findViewById<TextView>(R.id.nameCureNoteReady)
        val doseText = itemView.findViewById<TextView>(R.id.doseCureNoteReady)
        val timeText = itemView.findViewById<TextView>(R.id.timeCureNoteReady)
        val editBtn = itemView.findViewById<Button>(R.id.changeBtnNotifReady)
        val dellBtn = itemView.findViewById<Button>(R.id.delBtnNotifReady)
        val acceptBtn = itemView.findViewById<Button>(R.id.acceptBtnReady)
        val putoffBtn = itemView.findViewById<Button>(R.id.putoffBtnReady)
        fun bind(notification: NotificationWithCure){
            titleText.setText(notification.cure.title)
            doseText.setText("Дозировка: " + notification.cure.dosage.toString())

            var time = "00:00"
            if(notification.notification.minutes < 10){
                time = notification.notification.hours.toString() + ":0" + notification.notification.minutes.toString()
            } else{
                time = notification.notification.hours.toString() + ":" + notification.notification.minutes.toString()
            }
            timeText.setText(time)
        }
    }

    class waitingNotifHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText = itemView.findViewById<TextView>(R.id.nameCureNote)
        val doseText = itemView.findViewById<TextView>(R.id.doseCureNote)
        val timeText = itemView.findViewById<TextView>(R.id.timeCureNote)
        val editBtn = itemView.findViewById<Button>(R.id.changeBtnNotif)
        val dellBtn = itemView.findViewById<Button>(R.id.delBtnNotif)
        fun bind(notification: NotificationWithCure){
            titleText.setText(notification.cure.title)
            doseText.setText("Дозировка: " + notification.cure.dosage.toString())

            var time = "00:00"
            if(notification.notification.minutes < 10){
                time = notification.notification.hours.toString() + ":0" + notification.notification.minutes.toString()
            } else{
                time = notification.notification.hours.toString() + ":" + notification.notification.minutes.toString()
            }
            timeText.setText(time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        if(viewType == READY_TYPE){
            val view = LayoutInflater.from(context).inflate(R.layout.notification_recycler_item_ready, parent, false)
            return readyNotifHolder(view)
        }
        else {
            val view = LayoutInflater.from(context).inflate(R.layout.notification_recycler_item, parent, false)
            return waitingNotifHolder(view)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemViewType(position: Int): Int {
        val dnow = LocalDateTime.now().plusHours(7)
        val dateNow = LocalDate.now()
        var dlate = LocalDateTime.of(dateNow, LocalTime.of(notifications[position].notification.hours, notifications[position].notification.minutes)).plusMinutes(5)
        var difference = Duration.between(dnow, dlate).seconds/60

        if(difference < 0){
            dlate = LocalDateTime.of(dateNow, LocalTime.of(15, 0)).plusDays(1).plusMinutes(5)
            difference = Duration.between(dnow, dlate).seconds/60
        }

        if(difference <= 10){   // "за" и "после" 5 минут от дедлайна
            return READY_TYPE
        } else {
            return WAITING_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = notifications[position]

        if(holder.itemViewType == READY_TYPE){
            (holder as readyNotifHolder).bind(data)
            holder.editBtn.setOnClickListener {
                notifEdit.onEditClick(data)
            }
            holder.dellBtn.setOnClickListener {
                notifDel.onDeleteClick(data.notification)
            }
        }

        if(holder.itemViewType == WAITING_TYPE){
            (holder as waitingNotifHolder).bind(data)
            holder.editBtn.setOnClickListener {
                notifEdit.onEditClick(data)
            }
            holder.dellBtn.setOnClickListener {
                notifDel.onDeleteClick(data.notification)
            }
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun updateList(newList: List<NotificationWithCure>){
        notifications.clear()
        notifications.addAll(newList)
        notifyDataSetChanged()
    }
}

interface NotifClickDeleteInterface {
    fun onDeleteClick(notification: Notification)
}

interface NotifClickEditInterface {
    fun onEditClick(notification: NotificationWithCure)
}
