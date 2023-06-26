package com.example.app.data.repository

import androidx.annotation.WorkerThread
import com.example.app.data.dao.UtentePossiedeBadgeUtenteDAO
import com.example.app.data.relation.UtenteBadgeRistoranteCrossRef
import com.example.app.data.relation.UtenteBadgeUtenteCrossRef
import com.example.app.data.relation.UtentePossiedeBadgeUtente
import kotlinx.coroutines.flow.Flow

class UtentePossiedeBadgeUtenteRepository(private val utentePossiedeBadgeUtenteDAO: UtentePossiedeBadgeUtenteDAO) {

    val utentiBadgeUtente: Flow<List<UtentePossiedeBadgeUtente>> = utentePossiedeBadgeUtenteDAO.getUtentiBadgeUtente()

    val utenteBadgeUtenteRef: Flow<List<UtenteBadgeUtenteCrossRef>> = utentePossiedeBadgeUtenteDAO.getBadgeUtentiRef()

    @WorkerThread
    suspend fun newBadgeUtenteForUtente(newBadge: UtenteBadgeUtenteCrossRef) {
        utentePossiedeBadgeUtenteDAO.newBadgeUtenteForUtente(newBadge)
    }
}