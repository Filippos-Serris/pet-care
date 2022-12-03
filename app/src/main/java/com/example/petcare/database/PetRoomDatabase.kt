package com.example.petcare.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.petcare.database.medication.Medication
import com.example.petcare.database.pet.Pet
import com.example.petcare.database.pet.PetDao
import com.example.petcare.database.vaccine.Vaccine
import com.example.petcare.database.vaccine.VaccineDao

@Database(
    entities = [Pet::class, Vaccine::class, Medication::class],
    version = 6,
    exportSchema = false
)
abstract class PetRoomDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun vaccineDao(): VaccineDao

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