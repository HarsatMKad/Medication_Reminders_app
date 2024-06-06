package ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medication_reminders_app.data.Journal
import com.example.medication_reminders_app.data.JournalRepository
import com.example.medication_reminders_app.data.RemindersDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalViewModel(application: Application): AndroidViewModel(application) {
    var journalData: LiveData<List<Journal>> = MutableLiveData()
    val repository: JournalRepository

    init {
        val dao = RemindersDatabase.getDatabase(application).getJournalsDao()
        repository = JournalRepository(dao)
        journalData = repository.allJournals
    }

    fun addJournal(journal: Journal) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(journal)
    }

    fun deleteJournal(journal: Journal) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(journal)
    }

    fun updateJournal(journal: Journal) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(journal)
    }
}