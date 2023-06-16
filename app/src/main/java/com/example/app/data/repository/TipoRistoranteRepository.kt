package com.example.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.app.data.dao.TipoRistoranteDAO
import com.example.app.data.entity.TipoRistorante
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class TipoRistoranteRepository(private val tipoRistoranteDAO: TipoRistoranteDAO, private val context: Context) {

    val tipiRistorante: Flow<List<TipoRistorante>> = tipoRistoranteDAO.getTipiRistorante()

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tipi_preferences")
        private val TIPI_SELEZIONATI = stringPreferencesKey("tipi_selezionati")
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
            preferences[TIPI_SELEZIONATI]?:"111111"
        }

    suspend fun saveToDataStore(tipi: String) {
        context.dataStore.edit { preferences ->
            preferences[TIPI_SELEZIONATI] = tipi
        }
    }
}