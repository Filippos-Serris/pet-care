package com.example.petcare.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.petcare.database.bath.Bath
import com.example.petcare.database.bath.BathDao
import com.example.petcare.database.medication.MedicationDao
import kotlinx.coroutines.launch

class BathViewModel(private val bathDao: BathDao) : ViewModel() {
    private fun insertBath(bath: Bath) {
        viewModelScope.launch { bathDao.insertBath(bath) }
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