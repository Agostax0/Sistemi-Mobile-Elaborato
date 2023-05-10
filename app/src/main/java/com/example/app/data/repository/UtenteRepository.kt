package com.example.app.data.repository

import androidx.annotation.WorkerThread
import com.example.app.data.dao.UtenteDao
import com.example.app.data.entity.Utente
import kotlinx.coroutines.flow.Flow

class UtenteRepository(private val utenteDao: UtenteDao) {

    val utenti: Flow<List<Utente>> = utenteDao.getUtenti()

    @WorkerThread
    suspend fun insertNewUtente(utente: Utente) {
        utenteDao.insert(utente)
    }
}