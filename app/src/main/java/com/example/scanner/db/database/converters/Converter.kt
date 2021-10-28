package com.example.scanner.db.database.converters

import androidx.room.TypeConverter
import java.util.*

class Converter {

    @TypeConverter
    fun fromtimestamp(value:Long?): Date?{
        return value?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun datetotimestamp(date: Date?):Long?{
        return date?.time
    }
}