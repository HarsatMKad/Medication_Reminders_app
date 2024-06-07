package com.example.medication_reminders_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medication_reminders_app.data.Cure.Cure
import com.example.medication_reminders_app.data.Cure.CuresDao
import com.example.medication_reminders_app.data.Journal.Journal
import com.example.medication_reminders_app.data.Journal.JournalsDao
import com.example.medication_reminders_app.data.Notification.Notification
import com.example.medication_reminders_app.data.Notification.NotificationsDao

@Database(entities = arrayOf(Cure::class, Notification::class, Journal::class), version = 1, exportSchema = false)
abstract class RemindersDatabase : RoomDatabase() {
    abstract fun getCuresDao(): CuresDao
    abstract fun getNotificationsDao(): NotificationsDao
    abstract fun getJournalsDao(): JournalsDao

    companion object {
        @Volatile
        private var INSTANCE: RemindersDatabase? = null

        fun getDatabase(context: Context): RemindersDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RemindersDatabase::class.java,
                    "note_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }
}