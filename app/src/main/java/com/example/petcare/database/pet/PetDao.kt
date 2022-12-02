package com.example.petcare.database.pet

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PetDao {
    // Pet
    @Query("select * from pet order by name asc")
    fun getPets(): Flow<List<Pet>>

    @Query("select * from pet where petId = :id")
    fun getPet(id: Int): Flow<Pet>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPet(pet: Pet)

    @Delete
    suspend fun deletePet(pet: Pet)

    @Update
    suspend fun updatePet(pet: Pet)
}