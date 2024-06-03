package com.example.medication_reminders_app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cure")

class Cure(
    @ColumnInfo(name = "title")val title :String,
    @ColumnInfo(name = "description")val description :String,
    @ColumnInfo(name = "dosage")val dosage: Int,
    @ColumnInfo(name = "frequency")val frequency: String,
    @ColumnInfo(name = "instruction")val instruction: String) {
        @PrimaryKey(autoGenerate = true) var id = 0
}