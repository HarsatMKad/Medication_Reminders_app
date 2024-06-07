package View

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.medication_reminders_app.R
import com.example.medication_reminders_app.data.Notification.Notification
import com.example.medication_reminders_app.data.Notification.NotificationWithCure
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val READY_TYPE = 1
val WAITING_TYPE = 2
class NotificationAdapter(val context: Context,
                          val notifEdit: NotifClickEditInterface,
                          val notifDel: NotifClickDeleteInterface,
                          val notifAcept: NotifClickAcceptInterface,
                          val notifPutoff: NotifClickPutoffInterface,
                          val forTimerUpdate: timeOverInterface): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
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
        val timerText = itemView.findViewById<TextView>(R.id.difTimeNotif)
        val editBtn = itemView.findViewById<Button>(R.id.changeBtnNotif)
        val dellBtn = itemView.findViewById<Button>(R.id.delBtnNotif)
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(notification: NotificationWithCure){
            titleText.setText(notification.cure.title)
            doseText.setText("Дозировка: " + notification.cure.dosage.toString())

            val dnow = LocalDateTime.now().plusHours(7)
            var dateNow = LocalDate.now()
            if(notification.notification.hours <= 7){
                dateNow = LocalDate.now().plusDays(1)
            }
            var dlate = LocalDateTime.of(dateNow, LocalTime.of(notification.notification.hours, notification.notification.minutes))
            var difference = Duration.between(dnow, dlate).seconds/60

            if(difference < 0){
                dlate = LocalDateTime.of(dateNow, LocalTime.of(15, 0)).plusDays(1)
                difference = Duration.between(dnow, dlate).seconds/60
            }
            difference += notification.notification.days*60*24

            if(difference/60 >= 2) {
                timerText.text = (difference/60).toString() + " ч."
            } else {
                timerText.text = difference.toString() + " мин."
            }

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
        var dateNow = LocalDate.now()
        if(notifications[position].notification.hours <= 7){
            dateNow = LocalDate.now().plusDays(1)
        }
        var dlate = LocalDateTime.of(dateNow, LocalTime.of(notifications[position].notification.hours, notifications[position].notification.minutes)).plusMinutes(5)
        var difference = Duration.between(dnow, dlate).seconds/60

        if(difference < 0){
            dlate = LocalDateTime.of(dateNow, LocalTime.of(15, 0)).plusDays(1).plusMinutes(5)
            difference = Duration.between(dnow, dlate).seconds/60
        }

        difference += 24 * 60 * notifications[position].notification.days

        if(difference <= 10){   // "за" и "после" 5 минут от дедлайна
            return READY_TYPE
        } else {
            return WAITING_TYPE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = notifications[position]

        if(holder.itemViewType == READY_TYPE){
            (holder as readyNotifHolder).bind(data)

            val dnow = LocalDateTime.now().plusHours(7)
            var dateNow = LocalDate.now()
            if(data.notification.hours <= 7){
                dateNow = LocalDate.now().plusDays(1)
            }
            val dlate = LocalDateTime.of(dateNow, LocalTime.of(data.notification.hours, data.notification.minutes)).plusMinutes(10)
            val difference = Duration.between(dnow, dlate).toMillis()

            val o = object : CountDownTimer(difference, difference - 1) {
                override fun onTick(millisUntilFinished: Long) {
                    val dnow2 = LocalDateTime.now().plusHours(7)
                    var dateNow2 = LocalDate.now()
                    if(data.notification.hours <= 7){
                        dateNow2 = LocalDate.now().plusDays(1)
                    }
                    val dlate2 = LocalDateTime.of(dateNow2, LocalTime.of(data.notification.hours, data.notification.minutes)).plusMinutes(5)
                    val difference2 = Duration.between(dnow2, dlate2).seconds/60

                    if(difference2 <= 5){
                        forTimerUpdate.timeOverForNotification(data)
                    }
                }

                override fun onFinish() {

                }
            }.start()



            holder.editBtn.setOnClickListener {
                notifEdit.onEditClick(data)
            }
            holder.dellBtn.setOnClickListener {
                notifDel.onDeleteClick(data.notification)
            }
            holder.acceptBtn.setOnClickListener {
                o.onFinish()
                notifAcept.onAcceptClick(data)
            }
            holder.putoffBtn.setOnClickListener {
                notifPutoff.onPutoffClick(data.notification)
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

interface NotifClickAcceptInterface {
    fun onAcceptClick(notification: NotificationWithCure)
}

interface NotifClickPutoffInterface {
    fun onPutoffClick(notification: Notification)
}

interface timeOverInterface {
    fun timeOverForNotification(notification: NotificationWithCure)
}