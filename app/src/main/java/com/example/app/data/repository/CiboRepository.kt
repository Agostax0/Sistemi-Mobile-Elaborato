package com.example.app.data.repository

import com.example.app.data.dao.CiboDAO
import com.example.app.data.entity.Cibo
import kotlinx.coroutines.flow.Flow

class CiboRepository(private val ciboDAO: CiboDAO) {

    val cibi: Flow<List<Cibo>> = ciboDAO.getCibi()
}