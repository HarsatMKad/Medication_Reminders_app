package ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medication_reminders_app.data.Notification
import com.example.medication_reminders_app.data.NotificationRepository
import com.example.medication_reminders_app.data.NotificationWithCure
import com.example.medication_reminders_app.data.RemindersDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application): AndroidViewModel(application) {
    var notificationData: LiveData<List<Notification>> = MutableLiveData()
    val repository: NotificationRepository
    init {
        val dao = RemindersDatabase.getDatabase(application).getNotificationsDao()
        repository = NotificationRepository(dao)
        notificationData = repository.allNotifications
    }

    fun addNotification(notification: Notification) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(notification)
    }

    fun deleteNotification(notification: Notification) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(notification)
    }

    fun updateNotification(notification: Notification) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(notification)
    }
}