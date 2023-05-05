package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.app.data.entity.Cibo

@Dao
interface ristoranteMenuRistoranteDao{

    @Query("SELECT menu FROM menu_ristorante WHERE menu_ristorante.COD_RIS = :COD_RIS")
    fun getMenuOfRistorante(COD_RIS: Int) : List<Cibo>
}