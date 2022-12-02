package com.example.petcare

import android.app.Application
import com.example.petcare.database.PetRoomDatabase

class PetCareApplication : Application() {
    val database: PetRoomDatabase by lazy { PetRoomDatabase.getDatabase(this) }
}