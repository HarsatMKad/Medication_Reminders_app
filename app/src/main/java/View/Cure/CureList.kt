package View.Cure

import ViewModel.Cure.CureViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medication_reminders_app.R
import Model.Cure.Cure
import View.Journal.JournalList
import View.Notification.NotificationListActivity

class CureList : AppCompatActivity(), CureClickInterface, CureDeleteInterface, CureEditInterface {
    lateinit var viewModel: CureViewModel
    lateinit var recyclerViewCure: RecyclerView
    lateinit var buttonCreate: Button
    lateinit var buttonJournal: Button
    lateinit var buttonNotification: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cure_list)

        recyclerViewCure = findViewById(R.id.recyclerViewCure)
        buttonCreate = findViewById(R.id.buttonCreate)
        buttonJournal = findViewById(R.id.buttonJournal)
        buttonNotification = findViewById(R.id.buttonNotification)

        recyclerViewCure.layoutManager = LinearLayoutManager(this)

        val cureAdapter = CureAdapter(this, this, this, this)

        recyclerViewCure.adapter = cureAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(CureViewModel::class.java)

        viewModel.cureData.observe(this, Observer { list ->
            list?.let {
                cureAdapter.updateList(it)
            }
        })

        buttonCreate.setOnClickListener {
            val intent = Intent(this@CureList, CureCreate::class.java)
            startActivity(intent)
            this.finish()
        }

        buttonJournal.setOnClickListener {
            val intent = Intent(this@CureList, JournalList::class.java)
            startActivity(intent)
            this.finish()
        }

        buttonNotification.setOnClickListener {
            val intent = Intent(this@CureList, NotificationListActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onCureClick(cure: Cure) {
        val intent = Intent(this@CureList, CureSingle::class.java)
        intent.putExtra("cureTitle", cure.title)
        intent.putExtra("cureDescription", cure.description)
        intent.putExtra("cureDosage", cure.dosage)
        intent.putExtra("cureInstruction", cure.instruction)
        intent.putExtra("cureId", cure.id)
        startActivity(intent)
        this.finish()
    }

    override fun onDeleteClick(cure: Cure) {
        viewModel.deleteCure(cure)

        Toast.makeText(this, "${cure.title} успешно удалено!", Toast.LENGTH_LONG).show()
    }

    override fun onEditClick(cure: Cure) {
        val intent = Intent(this@CureList, CureCreate::class.java)
        intent.putExtra("typeOperation", "Edit")
        intent.putExtra("cureTitle", cure.title)
        intent.putExtra("cureDescription", cure.description)
        intent.putExtra("cureDosage", cure.dosage)
        intent.putExtra("cureInstruction", cure.instruction)
        intent.putExtra("cureId", cure.id)
        startActivity(intent)
        this.finish()
    }
}