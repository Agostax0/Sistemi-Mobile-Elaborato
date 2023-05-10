package com.example.app.data.repository

import com.example.app.data.dao.RistoranteMenuRistoranteDAO
import com.example.app.data.relation.RistoranteMenuRistorante
import kotlinx.coroutines.flow.Flow

class RistoranteMenuRistoranteRepository(private val ristoranteMenuRistoranteDAO: RistoranteMenuRistoranteDAO) {

    val menuRistoranti: Flow<List<RistoranteMenuRistorante>> = ristoranteMenuRistoranteDAO.getMenuRistoranti()
}