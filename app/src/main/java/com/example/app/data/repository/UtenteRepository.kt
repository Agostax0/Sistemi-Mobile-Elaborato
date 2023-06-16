package com.example.app.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.app.data.dao.UtenteDAO
import com.example.app.data.entity.Utente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UtenteRepository(private val utenteDAO:UtenteDAO, private val context: Context) {

    val utenti: Flow<List<Utente>> = utenteDAO.getUtenti()

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "logged_user_preferences")
        private val SESSION_INFO = stringPreferencesKey(name="session_info")
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
            preferences[SESSION_INFO]?: ""
        }

    suspend fun saveToDataStore(User: Utente) {
        context.dataStore.edit { preferences ->

            preferences[SESSION_INFO]= User.username
        }
    }

    suspend fun clearDataStore(){
        context.dataStore.edit {
                preferences -> preferences[SESSION_INFO] = ""
        }
    }


    @WorkerThread
    suspend fun getUtenteFromUsername(username:String): Utente?{
        return utenteDAO.getUtenteFromUsername(username)
    }

    fun checkUserLogin(username: String, password: String)=utenteDAO.checkLoginCredentials(username, password)

    @WorkerThread
    suspend fun insertNewUtente(utente: Utente) {
        utenteDAO.insert(utente)
    }
}