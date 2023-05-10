package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.app.data.relation.UtenteScansionaRistorante

@Dao
interface UtenteScansionaRistoranteDAO{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addScansioneRistorante(vararg utenteScansionaRistorante: UtenteScansionaRistorante)
}