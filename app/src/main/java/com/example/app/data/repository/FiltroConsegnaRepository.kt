package com.example.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.app.data.dao.FiltroConsegnaDAO
import com.example.app.data.entity.FiltroConsegna
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class FiltroConsegnaRepository(private val filtroConsegnaDAO: FiltroConsegnaDAO, private val context: Context) {

    val filtriConsegna: Flow<List<FiltroConsegna>> = filtroConsegnaDAO.getFiltriConsegna()

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filtri_preferences")
        private val FILTRI_SELEZIONATI = stringPreferencesKey("filtri_selezionati")
    }

    val preferenceFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[FILTRI_SELEZIONATI]?:"1111"
        }

    suspend fun saveToDataStore(filtri: String) {
        context.dataStore.edit { preferences ->
            preferences[FILTRI_SELEZIONATI] = filtri
        }
    }
}