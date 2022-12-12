package com.example.petcare.database.grooming

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.selects.select

@Dao
interface GroomingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGrooming(grooming: Grooming)

    @Transaction
    @Query("select * from grooming where petId=:id")
    fun getGroomings(id: Int): Flow<List<Grooming>>

    @Query("select * from grooming where groomingId=:id")
    fun getGrooming(id: Int): Flow<Grooming>

    @Update
    suspend fun updateGrooming(grooming: Grooming)
}