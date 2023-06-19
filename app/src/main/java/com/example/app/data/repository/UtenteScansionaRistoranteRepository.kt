package com.example.app.data.repository

import androidx.annotation.WorkerThread
import com.example.app.data.dao.UtenteScansionaRistoranteDAO
import com.example.app.data.entity.Utente
import com.example.app.data.relation.UtenteRistoranteCrossRef
import com.example.app.data.relation.UtenteScansionaRistorante
import kotlinx.coroutines.flow.Flow

class UtenteScansionaRistoranteRepository(private val utenteScansionaRistoranteDAO: UtenteScansionaRistoranteDAO) {

    val scansioniUtenti: Flow<List<UtenteScansionaRistorante>> = utenteScansionaRistoranteDAO.getScansioniUtenti()
    fun getRistorantiPreferitiPerUtente(ID:String):  Flow<List<Int>> {
        return utenteScansionaRistoranteDAO.getRistorantiPreferitiPerUtente(ID)
    }
    @WorkerThread
    suspend fun updatePreferito(utenteRistoranteCrossRef: UtenteRistoranteCrossRef) {
        utenteScansionaRistoranteDAO.updatePreferito(utenteRistoranteCrossRef)
    }
}