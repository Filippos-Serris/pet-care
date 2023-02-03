package com.example.petcare.database.exams

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamsDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExam(exams:Exams)

    @Transaction
    @Query("select * from exams where petId= :id")
    fun getExams(id:Int): Flow<List<Exams>>

    @Query("select * from exams where examId= :id")
    fun getExam(id:Int): Flow<Exams>

    @Update
    suspend fun updateExam(exams:Exams)
}