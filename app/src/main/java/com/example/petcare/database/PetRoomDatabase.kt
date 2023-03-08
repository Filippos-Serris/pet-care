package com.example.petcare.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.petcare.database.bath.Bath
import com.example.petcare.database.bath.BathDao
import com.example.petcare.database.exams.Exams
import com.example.petcare.database.exams.ExamsDao
import com.example.petcare.database.grooming.Grooming
import com.example.petcare.database.grooming.GroomingDao
import com.example.petcare.database.medication.Medication
import com.example.petcare.database.medication.MedicationDao
import com.example.petcare.database.pet.Pet
import com.example.petcare.database.pet.PetDao
import com.example.petcare.database.vaccine.Vaccine
import com.example.petcare.database.vaccine.VaccineDao

@Database(
    entities = [Pet::class, Vaccine::class, Medication::class, Bath::class, Grooming::class, Exams::class],
    version = 20,
    exportSchema = false
)
@TypeConverters(Converters::class)

abstract class PetRoomDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun vaccineDao(): VaccineDao
    abstract fun medicationDao(): MedicationDao
    abstract fun bathDao(): BathDao
    abstract fun groomingDao(): GroomingDao
    abstract fun examsDao(): ExamsDao

    companion object {
        @Volatile
        private var INSTANCE: PetRoomDatabase? = null

        fun getDatabase(context: Context): PetRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetRoomDatabase::class.java,
                    "pet_care_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}