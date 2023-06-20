package com.example.app.data.dao

import androidx.room.*
import com.example.app.data.relation.UtenteBadgeRistoranteCrossRef
import com.example.app.data.relation.UtenteRistoranteCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface UtentePossiedeBadgeRistoranteDAO {

    @Transaction
    @Query("SELECT * FROM UtenteBadgeRistoranteCrossRef")
    fun getUtentiBadgeRistorante() : Flow<List<UtenteBadgeRistoranteCrossRef>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun newBadgeRistoranteForUtente(vararg utentePossiedeBadgeRistorante: UtenteBadgeRistoranteCrossRef)
}
