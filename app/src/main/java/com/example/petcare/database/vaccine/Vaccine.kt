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
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "vaccineDescription") val vaccineDescription: String,
    @ColumnInfo(name = "vaccinationDate") val vaccinationDate: String,
    @ColumnInfo(name = "nextVaccinationDate") val nextVaccinationDate: String
)

/*data class PetVaccines(
    @Embedded val pet: Pet,
    @Relation(
        parentColumn = "petId",
        entityColumn = "petId"
    )
    val vaccines: List<Vaccine>
)*/