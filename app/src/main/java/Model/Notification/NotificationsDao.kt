package Model.Notification

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import Model.Notification.Notification
import Model.Notification.NotificationWithCure

@Dao
interface NotificationsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notification: Notification)

    @Delete
    suspend fun delete(notification: Notification)

    @Query("Select * from notification")
    fun getAllNotifications(): LiveData<List<Notification>>

    @Query("Select * from notification")
    fun getNotificationWithCure(): LiveData<List<NotificationWithCure>>

    @Update
    suspend fun update(notification: Notification)
}