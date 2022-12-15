package com.example.petcare.viewmodels

import androidx.lifecycle.*
import com.example.petcare.database.grooming.Grooming
import com.example.petcare.database.grooming.GroomingDao
import kotlinx.coroutines.launch

class GroomingViewModel(private val groomingDao: GroomingDao) : ViewModel() {
    private fun insertGrooming(grooming: Grooming) {
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

    //-------------------------------------------------------------------

    private fun getNewGroomingEntry(
        petId: Int,
        groomingDate: String,
        nextGrooming: String
    ): Grooming {
        return Grooming(petId = petId, groomingDate = groomingDate, nextGroomingDate = nextGrooming)
    }

    fun addNewGrooming(
        petId: Int,
        groomingDate: String,
        nextGroomingDate: String
    ) {
        val newGrooming = getNewGroomingEntry(petId, groomingDate, nextGroomingDate)
        insertGrooming(newGrooming)
    }

    //---------------------------------------------------------------------------------------------------
    private fun getUpdatedGroomingEntry(
        groomingId: Int,
        petId: Int,
        groomingDate: String,
        nextGroomingDate: String
    ): Grooming {
        return Grooming(
            groomingId = groomingId,
            petId = petId,
            groomingDate = groomingDate,
            nextGroomingDate = nextGroomingDate
        )
    }

    fun updateGrooming(
        groomingId: Int,
        petId: Int,
        groomingDate: String,
        nextGroomingDate: String
    ) {
        val updatedGrooming =
            getUpdatedGroomingEntry(groomingId, petId, groomingDate, nextGroomingDate)
        updateGrooming(updatedGrooming)
    }

    // --------------------------------------------------------------------------------------------------

    fun isEntryValid(groomingDate: String): Boolean {
        if (groomingDate.isBlank())
            return false
        return true
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