package com.example.petcare.database.vaccine

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccineDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVaccine(vaccine: Vaccine)

    @Transaction
    @Query("select * from vaccine where petId = :id")
    fun getVaccines(id: Int): Flow<List<Vaccine>>

    @Query("select * from vaccine where vaccineId = :id")
    fun getVaccine(id: Int): Flow<Vaccine>

    @Update
    suspend fun updateVaccine(vaccine: Vaccine)
}