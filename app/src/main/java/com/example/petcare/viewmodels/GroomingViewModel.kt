package com.example.petcare.viewmodels

import androidx.lifecycle.*
import com.example.petcare.database.grooming.Grooming
import com.example.petcare.database.grooming.GroomingDao
import com.example.petcare.database.medication.MedicationDao
import kotlinx.coroutines.launch

class GroomingViewModel(private val groomingDao: GroomingDao) : ViewModel() {
    private fun insetGrooming(grooming: Grooming) {
        viewModelScope.launch { groomingDao.insertGrooming(grooming) }
    }

    fun retrieveGroomings(id: Int): LiveData<List<Grooming>> {
        return groomingDao.getGroomings(id).asLiveData()
    }

    fun retrieveGrooming(id: Int): LiveData<Grooming> {
        return groomingDao.getGrooming(id).asLiveData()
    }

    private fun updateGrooming(grooming: Grooming) {
        viewModelScope.launch { groomingDao.updateGrooming(grooming) }
    }
}

class GroomingViewModelFactory(private val groomingDao: GroomingDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroomingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroomingViewModel(groomingDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}