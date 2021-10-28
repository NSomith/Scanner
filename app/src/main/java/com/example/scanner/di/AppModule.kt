package com.example.scanner.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scanner.db.database.AppDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun provideDatabase(context: Context){
    Room.databaseBuilder(
        context,
        AppDb::class.java,
        "scanner_db"
    ).build()
}

val appmodule= module {
    single { provideDatabase(androidContext()) }
}