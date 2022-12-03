package com.example.petcare.database.medication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.petcare.database.pet.Pet

@Entity(
    tableName = "medication",
    foreignKeys = [ForeignKey(
        entity = Pet::class,
        childColumns = ["petId"],
        parentColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Medication(
    @PrimaryKey(autoGenerate = true) val medicationId: Int = 0,
    @ColumnInfo(name = "petId") val petId: Int,
    @ColumnInfo(name = "medicationDescription") val medicationDescription: String,
    @ColumnInfo(name = "medicationDosage") val medicationDosage: String,
    @ColumnInfo(name = "medicationStartDate") val medicationStartDate: String,
    @ColumnInfo(name = "medicationEndDate") val medicationEndDate: String,
    @ColumnInfo(name = "medicationRepetition") val medicationRepetition: String
)