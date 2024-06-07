package com.example.medication_reminders_app.data.Journal

import androidx.lifecycle.LiveData
import com.example.medication_reminders_app.data.Journal.Journal
import com.example.medication_reminders_app.data.Journal.JournalsDao

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