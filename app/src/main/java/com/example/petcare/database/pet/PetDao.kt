package com.example.petcare.database.pet

import androidx.room.*
import com.example.petcare.database.exams.Exams
import kotlinx.coroutines.flow.Flow


@Dao
interface PetDao {
    // Pet
    @Query("select * from pet order by name asc")
    fun getPets(): Flow<List<Pet>>

    @Query("select * from pet where petId = :id")
    fun getPet(id: Int): Flow<Pet>

    @Query("select * from exams where petId= :id")
    fun getExamsForDelete(id: Int): Flow<List<Exams>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPet(pet: Pet)

    @Delete
    suspend fun deletePet(pet: Pet)

    @Update
    suspend fun updatePet(pet: Pet)
}