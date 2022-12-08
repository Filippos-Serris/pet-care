package com.example.petcare.database.bath

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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
}