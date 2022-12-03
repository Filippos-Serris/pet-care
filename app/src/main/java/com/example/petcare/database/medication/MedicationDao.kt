package com.example.petcare.database.medication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMedication(medication: Medication)

    @Transaction
    @Query("select * from medication where petId= :id")
    fun getMedications(id: Int): Flow<List<Medication>>

    @Query("select * from medication where medicationId= :id")
    fun gerMedication(id: Int): Flow<Medication>

    @Update
    suspend fun updateMedication(medication: Medication)
}