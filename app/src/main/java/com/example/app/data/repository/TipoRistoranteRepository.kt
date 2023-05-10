package com.example.app.data.repository

import com.example.app.data.dao.TipoRistoranteDAO
import com.example.app.data.entity.TipoRistorante
import kotlinx.coroutines.flow.Flow

class TipoRistoranteRepository(private val tipoRistoranteDAO: TipoRistoranteDAO) {

    val tipiRistorante: Flow<List<TipoRistorante>> = tipoRistoranteDAO.getTipiRistorante()
}