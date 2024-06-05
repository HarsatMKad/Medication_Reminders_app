package View

import ViewModel.JournalViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medication_reminders_app.R
import com.example.medication_reminders_app.data.Journal

class JournalList : AppCompatActivity(), JournalDeleteInterface {
    lateinit var viewModel: JournalViewModel
    lateinit var recyclerViewCure: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cure_list)

        recyclerViewCure = findViewById(R.id.recyclerViewCure)

        recyclerViewCure.layoutManager = LinearLayoutManager(this)

        val journalAdapter = JournalAdapter(this, this)

        recyclerViewCure.adapter = journalAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(JournalViewModel::class.java)

        viewModel.journalData.observe(this, Observer { list ->
            list?.let {
                journalAdapter.updateList(it)
            }
        })
    }

    override fun onDeleteClick(journal: Journal) {
        viewModel.deleteJournal(journal)

        Toast.makeText(this, "${journal.cureTitle} успешно удалено!", Toast.LENGTH_LONG).show()
    }
}