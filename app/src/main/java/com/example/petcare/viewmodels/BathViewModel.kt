package com.example.petcare.viewmodels

import androidx.lifecycle.*
import com.example.petcare.database.bath.Bath
import com.example.petcare.database.bath.BathDao
import kotlinx.coroutines.launch

class BathViewModel(private val bathDao: BathDao) : ViewModel() {
    private fun insertBath(bath: Bath) {
        viewModelScope.launch { bathDao.insertBath(bath) }
    }

    fun retrieveBaths(id: Int): LiveData<List<Bath>> {
        return bathDao.getBaths(id).asLiveData()
    }

    fun retrieveBath(id: Int): LiveData<Bath> {
        return bathDao.getBath(id).asLiveData()
    }

    fun updateBath(bath: Bath) {
        viewModelScope.launch { bathDao.updateBath(bath) }
    }

    //------------------------------------------------------------------------

    private fun getNewBathEntry(petId: Int, bathDate: String, nextBathDate: String): Bath {
        return Bath(petId = petId, bathDate = bathDate, nextBathDate = nextBathDate)
    }

    fun addNewBath(petId: Int, bathDate: String, nextBathDate: String) {
        val newBath = getNewBathEntry(petId, bathDate, nextBathDate)
        insertBath(newBath)
    }

    //--------------------------------------------------------------------------------------------
    private fun getUpdateBathEntry(
        bathId: Int, petId: Int, bathDate: String, nextBathDate: String
    ): Bath {
        return Bath(
            bathId = bathId,
            petId = petId,
            bathDate = bathDate,
            nextBathDate = nextBathDate
        )
    }

    fun updateBath(bathId: Int, petId: Int, bathDate: String, nextBathDate: String) {
        val updatedBath = getUpdateBathEntry(bathId, petId, bathDate, nextBathDate)
        updateBath(updatedBath)
    }

    //--------------------------------------------------------------------------------------------

    fun isEntryValid(bathDate: String, nextBathDate: String): Boolean {
        if (bathDate.isBlank() || nextBathDate.isBlank())
            return false
        return true
    }
}

class BathViewModelFactory(private val bathDao: BathDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BathViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BathViewModel(bathDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}