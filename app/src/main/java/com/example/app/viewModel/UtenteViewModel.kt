package com.example.app.viewModel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.entity.Utente
import com.example.app.data.repository.UtenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtenteViewModel @Inject constructor(
    private val repository: UtenteRepository
) : ViewModel() {
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


    val utenti = repository.utenti

    private var _statoLogin:Utente? = null
    val statoLogin
        get() = _statoLogin

    private lateinit var _utenteLoggato:Utente
    val utenteLoggato
        get() = _utenteLoggato

    fun login(username:String, password:String){

        Log.d("LOGIN_TAG","attemping login with $username and $password")

        viewModelScope.launch {
            try {
                val loginResponse = repository.checkUserLogin(username, password)
                loginResponse.collect { utente ->
                    _statoLogin = utente
                }
            }
            catch (e: java.lang.Exception){
                return@launch
            }
        }

        Log.d("LOGIN_TAG","login: ${_statoLogin!=null}")

    }

    fun getUtenteLoggatoInfo(){
        viewModelScope.launch {
            try{
                val getInfoResponse = repository.getUtenteFromUsername(session.first())

                _utenteLoggato = getInfoResponse
            }
            catch (e: java.lang.Exception){
                return@launch
            }
        }
    }


    fun updateUsername(newUsername:String){

    }

    fun addNewUtente(utente: Utente) = viewModelScope.launch {
        repository.insertNewUtente(utente)
    }

}