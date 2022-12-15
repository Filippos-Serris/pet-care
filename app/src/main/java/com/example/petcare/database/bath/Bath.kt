package com.example.petcare.database.bath

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.petcare.database.pet.Pet

@Entity(
    tableName = "bath",
    foreignKeys = [ForeignKey(
        entity = Pet::class,
        childColumns = ["petId"],
        parentColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Bath(
    @PrimaryKey(autoGenerate = true) val bathId: Int = 0,
    @ColumnInfo(name = "petId") val petId: Int,
    @ColumnInfo(name = "bathDate") val bathDate: String,
    @ColumnInfo(name = "nextBathDate") val nextBathDate: String?
)