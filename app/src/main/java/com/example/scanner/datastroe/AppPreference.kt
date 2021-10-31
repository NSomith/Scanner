package com.example.scanner.datastroe

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore:DataStore<Preferences> by preferencesDataStore("pref")
class AppPreference(private val dataStore:DataStore<Preferences>) {
    private companion object{
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }

    val isFirstLaunch:Flow<Boolean>
        get() =
            dataStore.data.map {
                it[FIRST_LAUNCH] ?:false
            }

    suspend fun firstLaunchComplete() {
        dataStore.edit {
            it[FIRST_LAUNCH] = true
        }
    }
}