package com.example.petcare.database.medication

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMedication(medication: Medication)

    @Transaction
    @Query("select * from medication where petId= :id")
    fun getMedications(id: Int): Flow<List<Medication>>

    @Query("select * from medication where medicationId= :id")
    fun getMedication(id: Int): Flow<Medication>

    @Update
    suspend fun updateMedication(medication: Medication)

    @Delete
    suspend fun deleteMedication(medication: Medication)
}