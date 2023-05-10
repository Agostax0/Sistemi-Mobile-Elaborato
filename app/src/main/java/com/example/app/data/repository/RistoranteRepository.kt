package com.example.app.data.repository

import com.example.app.data.dao.RistoranteDAO
import com.example.app.data.entity.Ristorante
import kotlinx.coroutines.flow.Flow

class RistoranteRepository(private val ristoranteDAO: RistoranteDAO) {

    val ristoranti: Flow<List<Ristorante>> = ristoranteDAO.getRistoranti()
}