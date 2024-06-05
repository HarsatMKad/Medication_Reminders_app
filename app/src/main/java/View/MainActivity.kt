package View


import ViewModel.NotificationSender
import ViewModel.NotificationViewModel
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.medication_reminders_app.R
import com.example.medication_reminders_app.data.Notification


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button1)
        btn.setOnClickListener {
            val intent = Intent(this, NotificationListActivity::class.java)
            startActivity(intent)
        }
    }
}