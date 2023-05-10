package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.app.data.relation.RistoranteMenuRistorante
import kotlinx.coroutines.flow.Flow

@Dao
interface RistoranteMenuRistoranteDAO{

    @Transaction
    @Query("SELECT * FROM ristorante")
    fun getMenuRistoranti() : Flow<List<RistoranteMenuRistorante>>
    /*
    @Query("SELECT menu FROM menu_ristorante WHERE menu_ristorante.COD_RIS = :COD_RIS")
    fun getMenuOfRistorante(COD_RIS: Int) : List<Cibo>
    */
}