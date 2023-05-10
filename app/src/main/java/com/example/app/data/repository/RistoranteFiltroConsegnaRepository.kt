package com.example.app.data.repository

import com.example.app.data.dao.RistoranteFiltroConsegnaDAO
import com.example.app.data.relation.RistoranteFiltroConsegna
import kotlinx.coroutines.flow.Flow

class RistoranteFiltroConsegnaRepository(private val ristoranteFiltroConsegnaDAO: RistoranteFiltroConsegnaDAO) {

    val filtriRistoranti: Flow<List<RistoranteFiltroConsegna>> = ristoranteFiltroConsegnaDAO.getFiltriRistoranti()
}