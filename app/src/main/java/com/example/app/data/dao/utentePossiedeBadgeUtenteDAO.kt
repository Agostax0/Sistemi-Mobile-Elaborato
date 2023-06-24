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
    /*
    @Query("SELECT * FROM utente_possiede_badge_utente WHERE utente_possiede_badge_utente.ID = :userId")
    fun getBadgeUtenteObtainedOfUtente(userId:Int) : List<UtentePossiedeBadgeUtente>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun newBadgeUtenteForUtente(vararg utentePossiedeBadgeUtente: UtentePossiedeBadgeUtente)
    */
}