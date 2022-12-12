package com.example.petcare.database.grooming

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.petcare.database.pet.Pet

@Entity(
    tableName = "grooming",
    foreignKeys = [ForeignKey(
        entity = Pet::class,
        childColumns = ["petId"],
        parentColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Grooming(
    @PrimaryKey(autoGenerate = true) val groomingId: Int = 0,
    @ColumnInfo(name = "petId") val petId: Int,
    @ColumnInfo(name = "groomingDate") val groomingDate: String,
    @ColumnInfo(name = "nextGroomingDate") val nextGroomingDate: String
)