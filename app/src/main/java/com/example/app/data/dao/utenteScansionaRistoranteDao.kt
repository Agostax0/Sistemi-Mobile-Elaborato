package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.app.data.entity.UtenteScansionaRistorante

@Dao
interface utenteScansionaRistoranteDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addScansioneRistorante(vararg utenteScansionaRistorante: UtenteScansionaRistorante)
}