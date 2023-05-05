package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.app.data.entity.TipoRistorante

@Dao
interface ristoranteTipoRistoranteDao{

    @Query("SELECT tipi FROM tipo WHERE tipo.COD_RIS = :COD_RIS")
    fun getTipiOfRistorante(COD_RIS: Int) : List<TipoRistorante>
}