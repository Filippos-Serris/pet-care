package com.example.petcare.database.pet

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "pet")
data class Pet(
    @PrimaryKey(autoGenerate = true) val petId: Int = 0,
    @ColumnInfo(name="image") val petImage: String?,
    @ColumnInfo(name = "name") val petName: String,
    @ColumnInfo(name = "species") val petSpecies: String,
    @ColumnInfo(name = "breed") val petBreed: String?,
    @ColumnInfo(name = "sex") val petSex: String,
    @ColumnInfo(name = "dateOfBirth") val petDateOfBirth: String,
    @ColumnInfo(name = "colour") val petColour: String?,
    @ColumnInfo(name = "chip") val petChip: String?
)