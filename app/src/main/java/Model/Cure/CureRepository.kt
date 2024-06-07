package com.example.medication_reminders_app.data.Cure

import androidx.lifecycle.LiveData

class CureRepository(private val cureDao: CuresDao) {
    val allCures: LiveData<List<Cure>> = cureDao.getAllCures()

    suspend fun insert(cure: Cure) {
        cureDao.insert(cure)
    }

    suspend fun delete(cure: Cure){
        cureDao.delete(cure)
    }

    suspend fun update(cure: Cure){
        cureDao.update(cure)
    }
}