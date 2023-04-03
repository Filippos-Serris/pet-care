package com.example.petcare.database.bath

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BathDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBath(bath:Bath)

    @Transaction
    @Query("select * from bath where petId= :id")
    fun getBaths(id:Int): Flow<List<Bath>>

    @Query("select * from bath where bathId= :id")
    fun getBath(id:Int): Flow<Bath>

    @Update
    suspend fun updateBath(bath:Bath)

    @Delete
    suspend fun deleteBath(bath: Bath)
}