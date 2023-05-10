package com.example.app.data.repository

import com.example.app.data.dao.FiltroConsegnaDAO
import com.example.app.data.entity.FiltroConsegna
import kotlinx.coroutines.flow.Flow

class FiltroConsegnaRepository(private val filtroConsegnaDAO: FiltroConsegnaDAO) {

    val filtriConsegna: Flow<List<FiltroConsegna>> = filtroConsegnaDAO.getFiltriConsegna()
}