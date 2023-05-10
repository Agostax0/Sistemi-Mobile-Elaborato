package com.example.app.data.dao

import androidx.room.Query
import com.example.app.data.entity.FiltroConsegna
import kotlinx.coroutines.flow.Flow

interface FiltroConsegnaDAO{

    @Query(
        "SELECT * FROM filtro_consegna"
    )
    fun getFiltriConsegna() : Flow<List<FiltroConsegna>>
}