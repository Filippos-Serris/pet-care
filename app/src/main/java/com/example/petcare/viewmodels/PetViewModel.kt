package com.example.petcare.viewmodels

import androidx.lifecycle.*
import com.example.petcare.database.pet.Pet
import com.example.petcare.database.pet.PetDao
import kotlinx.coroutines.launch

class PetViewModel(
    private val petDao: PetDao
) : ViewModel() {
    //lateinit var pet: Pet

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