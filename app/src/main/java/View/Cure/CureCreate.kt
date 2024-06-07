package View.Cure

import ViewModel.Cure.CureViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.medication_reminders_app.R
import Model.Cure.Cure

class CureCreate : AppCompatActivity() {
    lateinit var cureTitle: EditText
    lateinit var cureDescription: EditText
    lateinit var cureInstruction: EditText
    lateinit var cureDosage: EditText
    lateinit var saveButton: Button
    lateinit var backButton: Button

    lateinit var viewModel: CureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cure_create)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CureViewModel::class.java)

        cureTitle = findViewById(R.id.editTextName)
        cureDescription = findViewById(R.id.editTextDescription)
        cureInstruction = findViewById(R.id.editTextInstruction)
        cureDosage = findViewById(R.id.editTextDosage)
        saveButton = findViewById(R.id.buttonSave)
        backButton = findViewById(R.id.buttonBack)

        if (intent.getStringExtra("typeOperation").toString() == "Edit") {
            cureTitle.setText(intent.getStringExtra("cureTitle"))
            cureDescription.setText(intent.getStringExtra("cureDescription"))
            cureInstruction.setText(intent.getStringExtra("cureInstruction"))
            cureDosage.setText(intent.getIntExtra("cureDosage", 0).toString())
        }

        saveButton.setOnClickListener {
            if (cureTitle.text.toString().isNotEmpty() &&
                cureDescription.toString().isNotEmpty() &&
                cureDosage.toString().isNotEmpty() &&
                cureInstruction.toString().isNotEmpty()
            ) {
                val cure: Cure

                try {
                    cure = Cure(
                        cureTitle.text.toString(),
                        cureDescription.text.toString(),
                        cureDosage.text.toString().toInt(),
                        cureInstruction.text.toString()
                    )
                } catch (exception: Exception) {
                    Toast.makeText(this, "${exception}", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                if (intent.getStringExtra("typeOperation").toString() == "Edit") {
                    cure.id = intent.getIntExtra("cureId", 0)

                    viewModel.updateCure(cure)

                    Toast.makeText(
                        this,
                        "${cureTitle.text.toString()} Обновлён",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.addCure(cure)

                    Toast.makeText(
                        this,
                        "${cureTitle.text.toString()} Добавлен",
                        Toast.LENGTH_LONG
                    ).show()
                }

                startActivity(Intent(applicationContext, CureList::class.java))
                this.finish()
            } else {
                Toast.makeText(this, "Некоторые поля не заполнены!!!", Toast.LENGTH_LONG).show()
            }
        }

        backButton.setOnClickListener {
            startActivity(Intent(applicationContext, CureList::class.java))
            this.finish()
        }
    }
}