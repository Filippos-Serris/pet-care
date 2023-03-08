package com.example.petcare.database.exams

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.petcare.database.pet.Pet

@Entity(
    tableName = "exams",
    foreignKeys = [ForeignKey(
        entity = Pet::class,
        childColumns = ["petId"],
        parentColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Exams(
    @PrimaryKey(autoGenerate = true) val examId: Int = 0,
    @ColumnInfo(name = "petId") val petId: Int,
    @ColumnInfo(name = "type") val examType: String,
    @ColumnInfo(name = "description") val examDescription: String?,
    @ColumnInfo(name = "examinationDate") val examinationDate: String,
    @ColumnInfo(name = "nextExaminationDate") val nextExaminationDate: String?,
    @ColumnInfo(name = "results") val examinationResults: List<String>?
)