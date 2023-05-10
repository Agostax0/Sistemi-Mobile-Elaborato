package com.example.app.data.repository

import com.example.app.data.dao.UtenteScansionaRistoranteDAO
import com.example.app.data.relation.UtenteScansionaRistorante
import kotlinx.coroutines.flow.Flow

class UtenteScansionaRistoranteRepository(private val utenteScansionaRistoranteDAO: UtenteScansionaRistoranteDAO) {

    val scansioniUtenti: Flow<List<UtenteScansionaRistorante>> = utenteScansionaRistoranteDAO.getScansioniUtenti()
}