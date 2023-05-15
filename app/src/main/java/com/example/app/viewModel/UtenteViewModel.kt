package com.example.app.viewModel

import androidx.annotation.WorkerThread
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

     fun login(username:String, password:String){
        viewModelScope.launch {
            val utente:Utente? = repository.getUtenteFromUsername(username)

            if(utente!=null){
                if(utente.password.equals(password)){
                    _utenteLoggato = utente
                }
            }
        }
    }




    fun addNewUtente(utente: Utente) = viewModelScope.launch {
        repository.insertNewUtente(utente)
    }
}