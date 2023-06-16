package com.example.app.viewModel

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.entity.Utente
import com.example.app.data.repository.UtenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class UtenteViewModel @Inject constructor(
    private val repository: UtenteRepository
) : ViewModel() {

    val utenti = repository.utenti

    private var _utenteLoggato:Utente? = null
    val utenteLoggato
        get() = _utenteLoggato

    fun login(username:String, password:String){

        Log.d("LOGIN_TAG","attemping login with $username and $password")

        viewModelScope.launch {
            try {
                val loginResponse = repository.checkUserLogin(username, password)
                loginResponse.collect { utente ->
                    _utenteLoggato = utente
                }
            }
            catch (e: java.lang.Exception){
                return@launch
            }
        }

        Log.d("LOGIN_TAG","login: ${_utenteLoggato!=null}")

    }

    val session = repository.preferenceFlow

    fun startSession(User: Utente){
        viewModelScope.launch {
            repository.saveToDataStore(User)
        }

        Log.d("DATASTORE_TAG", "added ${User.username} into Datastore")
    }

    fun clearSession(){
        viewModelScope.launch {
            repository.clearDataStore()
        }
    }



    fun addNewUtente(utente: Utente) = viewModelScope.launch {
        repository.insertNewUtente(utente)
    }
}