package com.example.app.data.dao

import androidx.room.*
import com.example.app.data.relation.UtenteScansionaRistorante
import kotlinx.coroutines.flow.Flow

@Dao
interface UtenteScansionaRistoranteDAO{

    @Transaction
    @Query("SELECT * FROM utente")
    fun getScansioniUtenti() : Flow<List<UtenteScansionaRistorante>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addScansioneRistorante(vararg utenteScansionaRistorante: UtenteScansionaRistorante)
}