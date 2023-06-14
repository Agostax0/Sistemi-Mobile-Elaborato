package com.example.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.app.R
import com.example.app.data.entity.Utente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.Collections
/*
class SessionRepository(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "logged_user_preferences")

        private val SESSION_INFO = stringSetPreferencesKey(name="session_info")
    }

    val preferenceFlow: Flow<Set<String>?> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SESSION_INFO]?: null
        }

    suspend fun saveToDataStore(User: Utente) {
        context.dataStore.edit { preferences ->
            var session = HashSet<String>()

            session.


            preferences[SESSION_INFO] = session
        }
    }

    suspend fun clearDataStore(){

    }
}*/
