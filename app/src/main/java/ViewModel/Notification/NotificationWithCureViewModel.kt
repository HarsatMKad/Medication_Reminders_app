package ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import Model.Notification.NotificationRepository
import Model.Notification.NotificationWithCure
import Model.RemindersDatabase

class NotificationWithCureViewModel(application: Application): AndroidViewModel(application) {
    var notificationData: LiveData<List<NotificationWithCure>> = MutableLiveData()
    val repository: NotificationRepository

    init {
        val dao = RemindersDatabase.getDatabase(application).getNotificationsDao()
        repository = NotificationRepository(dao)
        notificationData = repository.notificationWithCure
    }
}