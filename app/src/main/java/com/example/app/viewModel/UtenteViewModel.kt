package com.example.app.viewModel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.Utente
import com.example.app.data.repository.UtenteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UtenteViewModel @Inject constructor(
    private val repository: UtenteRepository
) : ViewModel() {
    val session = repository.preferenceFlow

    fun startSession(User: Utente) {
        viewModelScope.launch {
            repository.saveToDataStore(User)
        }
    }

    fun clearSession(){
        viewModelScope.launch {
            repository.clearDataStore()
            _utenteLoggato = null
        }
    }

    val utenti = repository.utenti

    private var _utenteLoggato: Utente? = null
    val utenteLoggato
        get() = _utenteLoggato

    fun selectutente(utente: Utente) {
        _utenteLoggato = utente
    }

    fun addNewUtente(utente: Utente) = viewModelScope.launch {
        repository.insertNewUtente(utente)
    }

}