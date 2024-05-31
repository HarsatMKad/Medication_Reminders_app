package View

import ViewModel.NotificationSender
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.medication_reminders_app.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.Date
import kotlin.math.min
import kotlin.time.TimeSource

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

        val nameText = findViewById<EditText>(R.id.nameText)
        val doseText = findViewById<EditText>(R.id.doseText)
        val repeatText = findViewById<EditText>(R.id.repeatText)
        val acceptBtn = findViewById<Button>(R.id.acceptBtn)
        val backBtn = findViewById<Button>(R.id.backBtn)

        val timeBtn = findViewById<Button>(R.id.getTimeBtn)

        savedMinute.observe(this, Observer {

            if(savedMinute.value!! < 10){
                timeView.text = savedHour.value.toString() + ":0" + savedMinute.value.toString()
            } else{
                timeView.text = savedHour.value.toString() + ":" + savedMinute.value.toString()
            }
        })

        timeBtn.setOnClickListener{
            TimePickerDialog(this, this, hour, minute, true).show()
        }

        acceptBtn.setOnClickListener{
            val name = nameText.text
            val dose = doseText.text
            val period = repeatText.text

            val a = NotificationSender(this)
            val title = "Принять таблетки: " + name
            val description = "Принять " + name + ". Дозировка: " + dose
            val mainText = "Напоминание принять таблетки " + name + " в " + savedHour.value.toString() + ":" + savedMinute.value.toString() + ". Дозировка: " + dose

            a.addSend(title, description, mainText, savedHour.value!!, savedMinute.value!!, period.toString().toLong(), "test")
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour.value = hourOfDay
        savedMinute.value = minute
    }
}