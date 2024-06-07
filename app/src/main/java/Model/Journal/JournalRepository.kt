package Model.Journal

import androidx.lifecycle.LiveData
import Model.Journal.Journal
import Model.Journal.JournalsDao

class JournalRepository(private val journalDao: JournalsDao) {
    val allJournals: LiveData<List<Journal>> = journalDao.getAllJournals()

    suspend fun insert(journal: Journal) {
        journalDao.insert(journal)
    }

    suspend fun delete(journal: Journal){
        journalDao.delete(journal)
    }

    suspend fun update(journal: Journal){
        journalDao.update(journal)
    }
}