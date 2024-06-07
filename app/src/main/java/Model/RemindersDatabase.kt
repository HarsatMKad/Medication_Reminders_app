package Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import Model.Cure.Cure
import Model.Cure.CuresDao
import Model.Journal.Journal
import Model.Journal.JournalsDao
import Model.Notification.Notification
import Model.Notification.NotificationsDao

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