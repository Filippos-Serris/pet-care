package com.example.petcare.viewmodels

import androidx.lifecycle.*
import com.example.petcare.database.vaccine.Vaccine
import com.example.petcare.database.vaccine.VaccineDao
import kotlinx.coroutines.launch

class VaccineViewModel(private val vaccineDao: VaccineDao) : ViewModel() {
    private fun insertVaccine(vaccine: Vaccine) {
        viewModelScope.launch { vaccineDao.insertVaccine(vaccine) }
    }

    fun retrieveVaccines(id: Int): LiveData<List<Vaccine>> {
        return vaccineDao.getVaccines(id).asLiveData()
    }

    fun retrieveVaccine(id: Int): LiveData<Vaccine> {
        return vaccineDao.getVaccine(id).asLiveData()
    }

    private fun updateVaccine(vaccine: Vaccine) {
        viewModelScope.launch { vaccineDao.updateVaccine(vaccine) }
    }

    private fun getNewVaccineEntry(
        petId: Int,
        vaccineName: String,
        vaccineDescription: String,
        vaccinationDate: String,
        nextVaccinationDate: String
    ): Vaccine {
        return Vaccine(
            petId = petId,
            vaccineName = vaccineName,
            vaccineDescription = vaccineDescription,
            vaccinationDate = vaccinationDate,
            nextVaccinationDate = nextVaccinationDate
        )
    }

    fun addNewVaccine(
        petId: Int,
        vaccineName: String,
        vaccineDescription: String,
        vaccinationDate: String,
        nextVaccinationDate: String
    ) {
        val newVaccine = getNewVaccineEntry(
            petId,
            vaccineName,
            vaccineDescription,
            vaccinationDate,
            nextVaccinationDate
        )
        insertVaccine(newVaccine)
    }

    // -------------------- Update Vaccine ---------------------------
    private fun getUpdatedVaccineEntry(
        vaccineId: Int,
        petId: Int,
        vaccineName: String,
        vaccineDescription: String,
        vaccinationDate: String,
        nextVaccinationDate: String
    ): Vaccine {
        return Vaccine(
            vaccineId = vaccineId,
            petId = petId,
            vaccineName = vaccineName,
            vaccineDescription = vaccineDescription,
            vaccinationDate = vaccinationDate,
            nextVaccinationDate = nextVaccinationDate
        )
    }

    fun updateVaccine(
        vaccineId: Int,
        petId: Int,
        vaccineName: String,
        vaccineDescription: String,
        vaccinationDate: String,
        nextVaccinationDate: String
    ) {
        val updatedVaccine = getUpdatedVaccineEntry(
            vaccineId,
            petId,
            vaccineName,
            vaccineDescription,
            vaccinationDate,
            nextVaccinationDate
        )
        updateVaccine(updatedVaccine)
    }
    // ---------------------------------------------------------------

    fun isEntryValid(
        vaccineName: String,
        vaccineDescription: String,
        vaccinationDate: String,
        nextVaccinationDate: String
    ): Boolean {
        if (vaccineName.isBlank() || vaccineDescription.isBlank() || vaccinationDate.isBlank() || nextVaccinationDate.isBlank()) {
            return false
        }
        return true
    }
}

class VaccineViewModelFactory(private val vaccineDao: VaccineDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VaccineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VaccineViewModel(vaccineDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}