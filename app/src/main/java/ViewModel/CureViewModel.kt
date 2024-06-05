package ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medication_reminders_app.data.Cure
import com.example.medication_reminders_app.data.CureRepository
import com.example.medication_reminders_app.data.Notification
import com.example.medication_reminders_app.data.NotificationRepository
import com.example.medication_reminders_app.data.RemindersDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CureViewModel(application: Application): AndroidViewModel(application) {
    var cureData: LiveData<List<Cure>> = MutableLiveData()
    val repository: CureRepository

    init {
        val dao = RemindersDatabase.getDatabase(application).getCuresDao()
        repository = CureRepository(dao)
        cureData = repository.allCures
    }

    fun addCure(cure: Cure) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(cure)
    }

    fun deleteCure(cure: Cure) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(cure)
    }
}