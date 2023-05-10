package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.app.data.relation.RistoranteFiltroConsegna
import kotlinx.coroutines.flow.Flow

@Dao
interface RistoranteFiltroConsegnaDAO{
    @Transaction
    @Query("SELECT * FROM ristorante")
    fun getFiltriRistoranti() : Flow<List<RistoranteFiltroConsegna>>
    /*
    @Query("SELECT filtri FROM filtro_consegna_ristorante WHERE COD_RIS = :RIS_ID")
    fun getFiltriOfRistorante(RIS_ID: Int) : List<FiltroConsegna>

    //TODO prototipo
    @Query("SELECT * FROM filtro_consegna_ristorante WHERE filtri = :filtri")
    fun getRistorantiWithFiltri(filtri: List<FiltroConsegna>) : List<RistoranteFiltroConsegna>

    //altrimenti
    @Query("SELECT * FROM ristorante JOIN filtro_consegna_ristorante ON ristorante.COD_RIS = filtro_consegna_ristorante.COD_RIS " +
            "WHERE filtro_consegna_ristorante.filtri = :filtri")
    fun getRistorantiWithFiltri2(filtri: List<FiltroConsegna>) : List<Ristorante>
    */
}