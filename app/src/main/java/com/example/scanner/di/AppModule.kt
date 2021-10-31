package com.example.scanner.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scanner.datastroe.AppPreference
import com.example.scanner.datastroe.dataStore
import com.example.scanner.db.database.AppDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.prefs.Preferences

fun provideDatabase(context: Context){
    Room.databaseBuilder(
        context,
        AppDb::class.java,
        "scanner_db"
    ).build()
}
private fun providePreferences(context: Context) = AppPreference(context.dataStore)


val appmodule= module {
    single { provideDatabase(androidContext()) }
    factory { providePreferences(androidContext()) }
}