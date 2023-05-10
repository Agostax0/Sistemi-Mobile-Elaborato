package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.app.data.entity.BadgeUtente

@Dao
interface BadgeUtenteDao{
    @Query(
        "SELECT * FROM badge_utente " +
                "WHERE badge_utente.COD_BU = :ID"
    )
    fun getBadge(ID: Int) : BadgeUtente
}