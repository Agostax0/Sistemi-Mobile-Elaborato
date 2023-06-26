package com.example.app.data.dao

import androidx.room.*
import com.example.app.data.relation.UtenteBadgeUtenteCrossRef
import com.example.app.data.relation.UtentePossiedeBadgeUtente
import kotlinx.coroutines.flow.Flow

@Dao
interface UtentePossiedeBadgeUtenteDAO{

    @Transaction
    @Query("SELECT * FROM utente")
    fun getUtentiBadgeUtente() : Flow<List<UtentePossiedeBadgeUtente>>

    @Query("SELECT * FROM UtenteBadgeUtenteCrossRef")
    fun getBadgeUtentiRef(): Flow<List<UtenteBadgeUtenteCrossRef>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun newBadgeUtenteForUtente(vararg UtenteBadgeUtenteCrossRef: UtenteBadgeUtenteCrossRef)

}