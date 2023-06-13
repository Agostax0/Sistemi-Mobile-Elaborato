package com.example.app.viewModel

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.entity.Utente
import com.example.app.data.repository.UtenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UtenteViewModel @Inject constructor(
    private val repository: UtenteRepository
) : ViewModel() {

    val utenti = repository.utenti

    private var _utenteLoggato:Utente? = null
    val utenteLoggato
        get() = _utenteLoggato

    private var _statoLogin: LiveData<Utente>? = null
    val statoLogin
        get() = _statoLogin

    fun login(username:String, password:String){

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
    }





    fun addNewUtente(utente: Utente) = viewModelScope.launch {
        repository.insertNewUtente(utente)
    }
}