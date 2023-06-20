package com.example.app.data.repository

import androidx.annotation.WorkerThread
import com.example.app.data.dao.UtentePossiedeBadgeRistoranteDAO
import com.example.app.data.relation.UtenteBadgeRistoranteCrossRef
import com.example.app.data.relation.UtenteRistoranteCrossRef
import kotlinx.coroutines.flow.Flow

class UtentePossiedBadgeRistoranteRepository(private val utentePossiedeBadgeRistoranteDAO: UtentePossiedeBadgeRistoranteDAO) {

    val utentiBadgeRistorante: Flow<List<UtenteBadgeRistoranteCrossRef>> = utentePossiedeBadgeRistoranteDAO.getUtentiBadgeRistorante()

    @WorkerThread
    suspend fun newScansione(newBadge: UtenteBadgeRistoranteCrossRef) {
        utentePossiedeBadgeRistoranteDAO.newBadgeRistoranteForUtente(newBadge)
    }
}