package ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import Model.Cure.Cure
import Model.Cure.CureRepository
import Model.RemindersDatabase
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

    fun updateCure(cure: Cure) = viewModelScope.launch(Dispatchers.IO){
        repository.update(cure)
    }

    fun addCure(cure: Cure) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(cure)
    }

    fun deleteCure(cure: Cure) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(cure)
    }
}