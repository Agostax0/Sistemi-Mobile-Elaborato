package com.example.app.data.repository

import androidx.annotation.WorkerThread
import com.example.app.data.dao.UtenteDAO
import com.example.app.data.entity.Utente
import kotlinx.coroutines.flow.Flow

class UtenteRepository(private val utenteDAO:UtenteDAO) {

    val utenti: Flow<List<Utente>> = utenteDAO.getUtenti()

    @WorkerThread
    suspend fun insertNewUtente(utente: Utente) {
        utenteDAO.insert(utente)
    }
}