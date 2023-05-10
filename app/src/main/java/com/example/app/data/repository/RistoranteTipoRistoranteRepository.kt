package com.example.app.data.repository

import com.example.app.data.dao.RistoranteTipoRistoranteDAO
import com.example.app.data.relation.RistoranteTipoRistorante
import kotlinx.coroutines.flow.Flow

class RistoranteTipoRistoranteRepository(private val ristoranteTipoRistoranteDAO: RistoranteTipoRistoranteDAO) {

    val tipiRistoranti: Flow<List<RistoranteTipoRistorante>> = ristoranteTipoRistoranteDAO.getTipiRistoranti()
}