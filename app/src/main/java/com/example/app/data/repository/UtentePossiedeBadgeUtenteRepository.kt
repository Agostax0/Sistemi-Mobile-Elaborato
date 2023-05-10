package com.example.app.data.repository

import com.example.app.data.dao.UtentePossiedeBadgeUtenteDAO
import com.example.app.data.relation.UtentePossiedeBadgeUtente
import kotlinx.coroutines.flow.Flow

class UtentePossiedeBadgeUtenteRepository(private val utentePossiedeBadgeUtenteDAO: UtentePossiedeBadgeUtenteDAO) {

    val utentiBadgeUtente: Flow<List<UtentePossiedeBadgeUtente>> = utentePossiedeBadgeUtenteDAO.getUtentiBadgeUtente()
}