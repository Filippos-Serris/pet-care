package com.example.petcare.database.vaccine

import androidx.room.*
import com.example.petcare.database.pet.Pet

@Entity(
    tableName = "vaccine",
    foreignKeys = [ForeignKey(
        entity = Pet::class,
        childColumns = ["petId"],
        parentColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Vaccine(
    @PrimaryKey(autoGenerate = true) val vaccineId: Int = 0,
    @ColumnInfo(name = "petId") val petId: Int,
    @ColumnInfo(name = "name") val vaccineName: String,
    @ColumnInfo(name = "description") val vaccineDescription: String?,
    @ColumnInfo(name = "vaccinationDate") val vaccinationDate: String,
    @ColumnInfo(name = "nextVaccinationDate") val nextVaccinationDate: String
)