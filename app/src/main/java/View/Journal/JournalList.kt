package View

import ViewModel.JournalViewModel
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
import com.example.medication_reminders_app.data.Journal.Journal

class JournalList : AppCompatActivity(), JournalDeleteInterface {
    lateinit var viewModel: JournalViewModel
    lateinit var recyclerViewJournal: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_list)

        val backBtn = findViewById<Button>(R.id.buttonBack)
        recyclerViewJournal = findViewById(R.id.recyclerViewJournal)
        recyclerViewJournal.layoutManager = LinearLayoutManager(this)

        val journalAdapter = JournalAdapter(this, this)

        recyclerViewJournal.adapter = journalAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(JournalViewModel::class.java)

        viewModel.journalData.observe(this, Observer { list ->
            list?.let {
                journalAdapter.updateList(it)
            }
        })

        backBtn.setOnClickListener {
            val intent = Intent(this, CureList::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onDeleteClick(journal: Journal) {
        viewModel.deleteJournal(journal)

        Toast.makeText(this, "${journal.cureTitle} успешно удалено!", Toast.LENGTH_LONG).show()
    }
}