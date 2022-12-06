package com.example.petcare.viewmodels

import androidx.lifecycle.*
import com.example.petcare.database.medication.Medication
import com.example.petcare.database.medication.MedicationDao
import kotlinx.coroutines.launch

class MedicationViewModel(private val medicationDao: MedicationDao) : ViewModel() {

    private fun insertMedication(medication: Medication) {
        viewModelScope.launch { medicationDao.insertMedication(medication) }
    }

    fun retrieveMedications(id: Int): LiveData<List<Medication>> {
        return medicationDao.getMedications(id).asLiveData()
    }

    private fun updateMedication(medication: Medication) {
        viewModelScope.launch { medicationDao.updateMedication(medication) }
    }

    private fun getNewMedicationEntry(
        petId: Int,
        medicationName: String,
        medicationDescription: String,
        medicationDosage: String,
        medicationStartDate: String,
        medicationEndDate: String,
        medicationRepetition: String
    ): Medication {
        return Medication(
            petId = petId,
            medicationName = medicationName,
            medicationDescription = medicationDescription,
            medicationDosage = medicationDosage,
            medicationStartDate = medicationStartDate,
            medicationEndDate = medicationEndDate,
            medicationRepetition = medicationRepetition
        )
    }

    fun addNewMedication(
        petId: Int,
        medicationName: String,
        medicationDescription: String,
        medicationDosage: String,
        medicationStartDate: String,
        medicationEndDate: String,
        medicationRepetition: String
    ) {
        val newMedication = getNewMedicationEntry(
            petId,
            medicationName,
            medicationDescription,
            medicationDosage,
            medicationStartDate,
            medicationEndDate,
            medicationRepetition
        )
        insertMedication(newMedication)
    }

    fun isEntryValid(
        medicationName: String,
        medicationDescription: String,
        medicationDosage: String,
        medicationStartDate: String,
        medicationEndDate: String,
        medicationRepetition: String
    ): Boolean {
        if (medicationName.isBlank() || medicationDescription.isBlank() || medicationDosage.isBlank() || medicationStartDate.isBlank() || medicationEndDate.isBlank() || medicationRepetition.isBlank()) {
            return false
        }
        return true
    }
}

class MedicationViewModelFactory(private val medicationDao: MedicationDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicationViewModel(medicationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}