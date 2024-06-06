package View

import ViewModel.CureViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.medication_reminders_app.R

class CureSingle : AppCompatActivity() {
    lateinit var cureTitle: TextView
    lateinit var cureDescription: TextView
    lateinit var cureInstruction: TextView
    lateinit var cureDosage: TextView
    lateinit var cureFrequency: TextView
    lateinit var backButton: Button

    lateinit var viewModel: CureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cure_single)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CureViewModel::class.java)

        cureTitle = findViewById(R.id.textViewCureName)
        cureDescription = findViewById(R.id.textViewCureDescription)
        cureInstruction = findViewById(R.id.textViewCureInstruction)
        cureDosage = findViewById(R.id.textViewDosage)
        cureFrequency = findViewById(R.id.textViewFrequency)
        backButton = findViewById(R.id.buttonBack)

        cureTitle.setText(intent.getStringExtra("cureTitle"))
        cureDescription.setText(intent.getStringExtra("cureDescription"))
        cureInstruction.setText(intent.getStringExtra("cureInstruction"))
        cureDosage.setText(intent.getIntExtra("cureDosage", 0).toString() + " мг")
        cureFrequency.setText(intent.getLongExtra("cureFrequency", 0).toString())

        backButton.setOnClickListener {
            startActivity(Intent(applicationContext, CureList::class.java))
            this.finish()
        }
    }
}