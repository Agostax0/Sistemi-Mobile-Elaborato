package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.app.data.relation.RistoranteTipoRistorante
import kotlinx.coroutines.flow.Flow

@Dao
interface RistoranteTipoRistoranteDAO {
    @Transaction
    @Query("SELECT * FROM ristorante")
    fun getTipiRistoranti() : Flow<List<RistoranteTipoRistorante>>
}
