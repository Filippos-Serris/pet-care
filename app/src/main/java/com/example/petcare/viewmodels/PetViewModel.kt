package com.example.petcare.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.*
import com.example.petcare.database.pet.Pet
import com.example.petcare.database.pet.PetDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Locale.US

class PetViewModel(
    private val petDao: PetDao
) : ViewModel() {

    val allPets: LiveData<List<Pet>> = petDao.getPets().asLiveData()

    private fun insertPet(pet: Pet) {
        viewModelScope.launch { petDao.insertPet(pet) }
    }

    fun retrievePet(id: Int): LiveData<Pet> {
        return petDao.getPet(id).asLiveData()
    }

    private fun updatePet(pet: Pet) {
        viewModelScope.launch { petDao.updatePet(pet) }
    }

    fun deletePet(pet: Pet) {
        viewModelScope.launch { petDao.deletePet(pet) }
    }

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

    //----------------------------------- Age of pet --------------------------------

    fun calculatePetAge(dateOfBirth: String): String {
        val currentDate = LocalDate.now()

        val petDateOfBirth = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("dd/MM/yyy"))

        val period = Period.between(petDateOfBirth, currentDate)
        val year = period.years

        return year.toString()
    }

    //-------------------------------------------------------------------------------
    fun isEntryValid(
        petName: String,
        petSpecies: String,
        petSex: String,
        petDateOfBirth: String
    ): Boolean {
        if (petName.isBlank() || petSpecies.isBlank() || petSex.isBlank() || petDateOfBirth.isBlank()) {
            return false
        }
        return true
    }
}

class PetViewModelFactory(private val petDao: PetDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PetViewModel(petDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}