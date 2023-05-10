package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.app.data.entity.TipoRistorante
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoRistoranteDAO{
    @Query("SELECT * FROM tipo_ristorante")
    fun getTipiRistorante() : Flow<List<TipoRistorante>>

    /*@Query("SELECT tipi FROM tipo WHERE tipo.COD_RIS = :COD_RIS")
    fun getTipiOfRistorante(COD_RIS: Int) : List<TipoRistorante>*/
}