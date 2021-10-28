package com.example.scanner.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scanner.db.dao.FilterTextModelDao
import com.example.scanner.db.dao.ScanDao
import com.example.scanner.db.database.converters.Converter
import com.example.scanner.db.entity.FilterTextModel
import com.example.scanner.db.entity.Scan

@Database(entities = [Scan::class,FilterTextModel::class],version = 1)
@TypeConverters(Converter::class)
abstract class AppDb :RoomDatabase(){
    abstract val scanDao: ScanDao
    abstract val filteredTextModelDao: FilterTextModelDao
}