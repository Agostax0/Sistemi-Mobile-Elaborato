package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app.data.relation.UtentePossiedeBadgeUtente

@Dao
interface UtentePossiedeBadgeUtenteDAO{

    @Query("SELECT * FROM utente_possiede_badge_utente WHERE utente_possiede_badge_utente.ID = :userId")
    fun getBadgeUtenteObtainedOfUtente(userId:Int) : List<UtentePossiedeBadgeUtente>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun newBadgeUtenteForUtente(vararg utentePossiedeBadgeUtente: UtentePossiedeBadgeUtente)
}