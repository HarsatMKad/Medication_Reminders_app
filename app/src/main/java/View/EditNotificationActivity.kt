package View

import ViewModel.CureViewModel
import ViewModel.NotificationSender
import ViewModel.NotificationViewModel
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.medication_reminders_app.R
import com.example.medication_reminders_app.data.Notification

class EditNotificationActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    var hour = 0
    var minute = 0
    var savedHour: MutableLiveData<Int> = MutableLiveData(0)
    var savedMinute: MutableLiveData<Int> = MutableLiveData(0)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_notification)

        val originNotification = intent.getSerializableExtra("notification") as SerializableNotification

        val timeView = findViewById<TextView>(R.id.timeViewEdit)
        val spinner = findViewById<Spinner>(R.id.spinner2)
        val repeatText = findViewById<EditText>(R.id.repeatTextEdit)
        val acceptBtn = findViewById<Button>(R.id.acceptBtnEdit)
        val backBtn = findViewById<Button>(R.id.backBtnEdit)
        val timeBtn = findViewById<Button>(R.id.getTimeBtnEdit)

        repeatText.setText(originNotification.period.toString())

        var time = "00:00"
        savedHour.value = originNotification.hours
        savedMinute.value = originNotification.minutes

        savedMinute.observe(this, Observer {
            if(savedMinute.value!! < 10){
                time = savedHour.value.toString() + ":0" + savedMinute.value.toString()
                timeView.text = time
            } else{
                time = savedHour.value.toString() + ":" + savedMinute.value.toString()
                timeView.text = time
            }
        })

        val viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            CureViewModel::class.java)
        viewModel.cureData.observe(this, Observer { list ->
            list.let {
                var listData: List<String> = listOf()
                var spinnerIndex = 0

                for(i in 0..it.size-1){
                    if(list[i].id == originNotification.cureId){
                        spinnerIndex = i
                    }
                    listData = listData.plus(it[i].title)
                }
                val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listData)
                spinner.adapter = spinnerAdapter
                spinner.setSelection(spinnerIndex)
                return@Observer
            }
        })

        acceptBtn.setOnClickListener {
            val cureViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
                CureViewModel::class.java)
            val notifViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
                NotificationViewModel::class.java)

            cureViewModel.cureData.observe(this, Observer { list ->
                list.let {
                    val selectedIndex = spinner.selectedItemPosition
                    val title = "Принять таблетки"
                    val description = "Принять " + it[selectedIndex].title + ". Дозировка: " + it[selectedIndex].dosage
                    val mainText = "Вы должны были принять " + it[selectedIndex].title + " в " +
                            time + ". Дозировка: " + it[selectedIndex].dosage
                    val period = repeatText.text.toString().toLong()
                    val cureId = it[selectedIndex].id

                    val notification = Notification(title, mainText, description, savedHour.value!!, savedMinute.value!!, period, cureId.toLong(), false, false)
                    notification.id = originNotification.id
                    notifViewModel.updateNotification(notification)
                    val sender = NotificationSender(baseContext)
                    sender.delSendByTag(originNotification.id.toString())
                    sender.addSend(notification)
                }
            })
            val intent = Intent(this, NotificationListActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        timeBtn.setOnClickListener {
            TimePickerDialog(this, this, hour, minute, true).show()
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, NotificationListActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour.value = hourOfDay
        savedMinute.value = minute
    }
}