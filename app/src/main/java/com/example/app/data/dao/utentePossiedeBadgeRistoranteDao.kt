package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app.data.entity.UtentePossiedeBadgeRistorante

@Dao
interface utentePossiedeBadgeRistoranteDao {

    @Query("SELECT * FROM utente_possiede_badge_ristorante WHERE utente_possiede_badge_ristorante.ID = :userId")
    fun getBagdeRistorantiObtainedOfUtente(userId:Int) : List<UtentePossiedeBadgeRistorante>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun newBadgeRistoranteForUtente(vararg utentePossiedeBadgeRistorante: UtentePossiedeBadgeRistorante)
}
