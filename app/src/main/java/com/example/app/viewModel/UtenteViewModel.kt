package com.example.app.viewModel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.Utente
import com.example.app.data.repository.UtenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
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

    fun stopCoroutines() {
        viewModelScope.cancel()
    }

    fun isActive(): Boolean {
        return viewModelScope.isActive
    }

    val utenti = repository.utenti

    private var _utenteLoggato: Utente? = null
    val utenteLoggato
        get() = _utenteLoggato

    fun selectutente(utente: Utente) {
        _utenteLoggato = utente
    }

    private var _statoLogin:Utente? = null
    val statoLogin
        get() = _statoLogin

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

    fun updateUsername(newUsername:String){

    }

    fun getSessionUsername():Int{
        var return_val: Int = 0

        viewModelScope.launch {
            try{
                val sessionUsername = session.firstOrNull {
                    true
                }

                if (sessionUsername == null) {

                    //session non ancora loaddata
                    return_val = 0
                }
                else if(sessionUsername!!.isEmpty()){
                    //session caricata ma vuota, quindi bisogna andare in login
                    return_val = 1
                }
                else{
                    //session caricata e non vuota, bisogna andare in home
                    return_val = 2
                }

            }
            catch (e: java.lang.Exception){
                return@launch
            }
        }

        Log.d("LOADING_TAG", "session state: $return_val")

        return return_val
    }

    fun addNewUtente(utente: Utente) = viewModelScope.launch {
        repository.insertNewUtente(utente)
    }

}