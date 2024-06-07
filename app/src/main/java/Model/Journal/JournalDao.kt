package com.example.medication_reminders_app.data.Journal

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.medication_reminders_app.data.Journal.Journal

@Dao
interface JournalsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(journal: Journal)

    @Delete
    suspend fun delete(journal: Journal)

    @Query("Select * from journal")
    fun getAllJournals(): LiveData<List<Journal>>

    @Update
    suspend fun update(journal: Journal)
}