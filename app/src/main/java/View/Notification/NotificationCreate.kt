package View

import ViewModel.CureViewModel
import ViewModel.NotificationSender
import ViewModel.NotificationViewModel
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.medication_reminders_app.R
import com.example.medication_reminders_app.data.Notification.Notification

class NotificationCreate : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    var hour = 0
    var minute = 0
    var savedHour: MutableLiveData<Int> = MutableLiveData(0)
    var savedMinute: MutableLiveData<Int> = MutableLiveData(0)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_create)

        val timeView = findViewById<TextView>(R.id.timeView)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val repeatText = findViewById<EditText>(R.id.repeatText)
        val acceptBtn = findViewById<Button>(R.id.acceptBtn)
        val backBtn = findViewById<Button>(R.id.backBtn)
        val timeBtn = findViewById<Button>(R.id.getTimeBtn)

        var time = "00:00"
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

                for(i in 0..it.size-1){
                    listData = listData.plus(it[i].title)
                }
                val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listData)
                spinner.adapter = spinnerAdapter
                return@Observer
            }
        })

        timeBtn.setOnClickListener{
            TimePickerDialog(this, this, hour, minute, true).show()
        }

        acceptBtn.setOnClickListener{
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

                    var period = 1
                    if(repeatText.text.toString().toInt() >= 1 && repeatText.text.toString().toInt() <= 99){
                        period = repeatText.text.toString().toInt()
                    }
                    val cureId = it[selectedIndex].id

                    val notification = Notification(title, mainText, description, savedHour.value!!, savedMinute.value!!, period - 1, period.toLong(), cureId.toLong())
                    notification.id = System.currentTimeMillis().toInt() * -1
                    notifViewModel.addNotification(notification)
                    val sender = NotificationSender(baseContext)
                    sender.addSend(notification)
                }
            })
            val intent = Intent(this, NotificationListActivity::class.java)
            startActivity(intent)
            this.finish()
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