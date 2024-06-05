package ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medication_reminders_app.data.Notification
import com.example.medication_reminders_app.data.NotificationRepository
import com.example.medication_reminders_app.data.NotificationWithCure
import com.example.medication_reminders_app.data.RemindersDatabase

class NotificationWithCureViewModel(application: Application): AndroidViewModel(application) {
    var notificationData: LiveData<List<NotificationWithCure>> = MutableLiveData()
    val repository: NotificationRepository

    init {
        val dao = RemindersDatabase.getDatabase(application).getNotificationsDao()
        repository = NotificationRepository(dao)
        notificationData = repository.notificationWithCure
    }
}