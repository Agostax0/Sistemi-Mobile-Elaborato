package com.example.app.data.dao

import androidx.room.*
import com.example.app.data.relation.UtentePossiedeBadgeRistorante
import kotlinx.coroutines.flow.Flow

@Dao
interface UtentePossiedeBadgeRistoranteDAO {

    @Transaction
    @Query("SELECT * FROM utente")
    fun getUtentiBadgeRistorante() : Flow<List<UtentePossiedeBadgeRistorante>>
    /*
    @Query("SELECT * FROM utente_possiede_badge_ristorante WHERE utente_possiede_badge_ristorante.ID = :userId")
    fun getBagdeRistorantiObtainedOfUtente(userId:Int) : List<UtentePossiedeBadgeRistorante>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun newBadgeRistoranteForUtente(vararg utentePossiedeBadgeRistorante: UtentePossiedeBadgeRistorante)
     */
}
