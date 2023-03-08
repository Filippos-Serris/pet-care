package com.example.petcare.database

import androidx.room.TypeConverter

private const val SEPARATOR = ","

class Converters {
    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return list.joinToString(separator = SEPARATOR)
    }

    @TypeConverter
    fun fromStringToList(string: String): List<String> {
        return string.split(SEPARATOR).toMutableList()
    }
}