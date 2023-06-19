package com.example.app.data.dao

import androidx.annotation.WorkerThread
import androidx.room.*
import com.example.app.data.relation.UtenteRistoranteCrossRef
import com.example.app.data.relation.UtenteScansionaRistorante
import kotlinx.coroutines.flow.Flow

@Dao
interface UtenteScansionaRistoranteDAO{

    @Transaction
    @Query("SELECT * FROM utente")
    fun getScansioniUtenti() : Flow<List<UtenteScansionaRistorante>>

    @Query("SELECT COD_RIS FROM UtenteRistoranteCrossRef WHERE ID=:ID AND preferito = 1")
    fun getRistorantiPreferitiPerUtente(ID: String): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addScansioneRistorante(vararg utenteRistoranteCrossRef: UtenteRistoranteCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePreferito(vararg utenteRistoranteCrossRef: UtenteRistoranteCrossRef)
}