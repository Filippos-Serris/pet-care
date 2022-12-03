package com.example.petcare.viewmodels

import androidx.lifecycle.*
import com.example.petcare.database.medication.MedicationDao
import com.example.petcare.database.pet.Pet
import com.example.petcare.database.pet.PetDao
import com.example.petcare.database.vaccine.Vaccine
import com.example.petcare.database.vaccine.VaccineDao
import kotlinx.coroutines.launch

class PetCareViewModel(
    private val petDao: PetDao,
    private val vaccineDao: VaccineDao,
) :
    ViewModel() {
    lateinit var pet: Pet

    val allPets: LiveData<List<Pet>> = petDao.getPets().asLiveData()

    //---------------------------------------------------------------------- Pet ----------------------------------------------------------------------------------------------------------
    // ---------------- Insert data to the database ---------------------
    private fun insertPet(pet: Pet) {
        viewModelScope.launch { petDao.insertPet(pet) }
    }

    fun retrievePet(id: Int): LiveData<Pet> {
        return petDao.getPet(id).asLiveData()
    }

    fun updatePet(pet: Pet) {
        viewModelScope.launch { petDao.updatePet(pet) }
    }

    fun deletePet(pet: Pet) {
        viewModelScope.launch { petDao.deletePet(pet) }
    }


    // ---------------------------- Insert Pet ---------------------------------
    private fun getNewPetEntry(
        petName: String,
        petSpecies: String,
        petBreed: String,
        petSex: String,
        petDateOfBirth: String,
        petColour: String,
        petChip: String
    ): Pet {
        return Pet(
            petName = petName,
            petSpecies = petSpecies,
            petBreed = petBreed,
            petSex = petSex,
            petDateOfBirth = petDateOfBirth,
            petColour = petColour,
            petChip = petChip
        )
    }

    fun addNewPet(
        petName: String,
        petSpecies: String,
        petBreed: String,
        petSex: String,
        petDateOfBirth: String,
        petColour: String,
        petChip: String
    ) {
        val newPet = getNewPetEntry(
            petName,
            petSpecies,
            petBreed,
            petSex,
            petDateOfBirth,
            petColour,
            petChip
        )
        insertPet(newPet)
    }

    // -------------------- Update Pet --------------------------
    private fun getUpdatedPetEntry(
        petId: Int,
        petName: String,
        petSpecies: String,
        petBreed: String,
        petSex: String,
        petDateOfBirth: String,
        petColour: String,
        petChip: String
    ): Pet {
        return Pet(
            petId = petId,
            petName = petName,
            petSpecies = petSpecies,
            petBreed = petBreed,
            petSex = petSex,
            petDateOfBirth = petDateOfBirth,
            petColour = petColour,
            petChip = petChip
        )
    }

    fun updatePet(
        petId: Int,
        petName: String,
        petSpecies: String,
        petBreed: String,
        petSex: String,
        petDateOfBirth: String,
        petColour: String,
        petChip: String
    ) {
        val updatedPet = getUpdatedPetEntry(
            petId,
            petName,
            petSpecies,
            petBreed,
            petSex,
            petDateOfBirth,
            petColour,
            petChip
        )
        updatePet(updatedPet)
    }

    fun isEntryValid(
        petName: String,
        petSpecies: String,
        petBreed: String,
        petSex: String,
        petDateOfBirth: String,
        petColour: String,
        petChip: String
    ): Boolean {
        if (petName.isBlank() || petSpecies.isBlank() || petBreed.isBlank() || petSex.isBlank() || petDateOfBirth.isBlank() || petColour.isBlank() || petChip.isBlank()) {
            return false
        }
        return true
    }

    // ---------------------------------------------------------------------------- Vaccine ---------------------------------------------------------------------------------------------

    private fun insertVaccine(vaccine: Vaccine) {
        viewModelScope.launch { vaccineDao.insertVaccine(vaccine) }
    }

    fun retrieveVaccines(id: Int): LiveData<List<Vaccine>> {
        return vaccineDao.getVaccines(id).asLiveData()
    }

    fun retrieveVaccine(id: Int): LiveData<Vaccine> {
        return vaccineDao.getVaccine(id).asLiveData()
    }

    fun updateVaccine(vaccine: Vaccine) {
        viewModelScope.launch { vaccineDao.updateVaccine(vaccine) }
    }

    // --------------------------- Insert Vaccine ------------------------
    private fun getNewVaccineEntry(
        petId: Int,
        vaccineName: String,
        vaccineDescription: String,
        vaccinationDate: String,
        nextVaccinationDate: String
    ): Vaccine {
        return Vaccine(
            petId = petId,
            name = vaccineName,
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
            name = vaccineName,
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

    // ------------------------------------------------------------------------------ Medication ----------------------------------------------------------------------------------------

}

class PetCareViewModelFactory(
    private val petDao: PetDao,
    private val vaccineDao: VaccineDao
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetCareViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetCareViewModel(petDao, vaccineDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}