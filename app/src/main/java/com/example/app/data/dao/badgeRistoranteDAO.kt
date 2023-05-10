package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.app.data.entity.BadgeRistorante
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeRistoranteDAO{
    @Query("SELECT * FROM badge_ristorante")
    fun getBadges(): Flow<List<BadgeRistorante>>

    @Query(
        "SELECT * FROM badge_ristorante " +
                "WHERE badge_ristorante.COD_BR = :ID"
    )
    fun getBadge(ID: Int) : BadgeRistorante

}