package com.example.petcare.viewmodels

import androidx.lifecycle.*
import com.example.petcare.R
import com.example.petcare.database.pet.Pet
import com.example.petcare.database.pet.PetDao
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class PetViewModel(
    private val petDao: PetDao
) : ViewModel() {

    private val _image = MutableLiveData<String>("")
    val image: LiveData<String> = _image

    private val _sex = MutableLiveData<String>("")
    val sex: LiveData<String> = _sex

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
        petImage: String,
        petName: String,
        petSpecies: String,
        petBreed: String,
        petSex: String,
        petDateOfBirth: String,
        petColour: String,
        petChip: String
    ): Pet {
        return Pet(
            petImage = petImage,
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
        petImage: String,
        petName: String,
        petSpecies: String,
        petBreed: String,
        petSex: String,
        petDateOfBirth: String,
        petColour: String,
        petChip: String
    ) {
        val newPet = getNewPetEntry(
            petImage,
            petName,
            petSpecies,
            petBreed,
            petSex,
            petDateOfBirth,
            petColour,
            petChip
        )
        insertPet(newPet)
        _sex.value = ""
    }

    fun setImage(image: String) {
        _image.value = image
    }

    fun setSex(sex: String) {
        _sex.value = sex
    }

    // -------------------- Update Pet --------------------------
    private fun getUpdatedPetEntry(
        petId: Int,
        petImage: String,
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
            petImage = petImage,
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
        petImage: String,
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
            petImage,
            petName,
            petSpecies,
            petBreed,
            petSex,
            petDateOfBirth,
            petColour,
            petChip
        )
        updatePet(updatedPet)
        _sex.value = ""
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