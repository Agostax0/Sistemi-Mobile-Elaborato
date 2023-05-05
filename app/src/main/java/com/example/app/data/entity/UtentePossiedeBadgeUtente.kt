package com.example.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import java.sql.Date

@Entity(tableName = "utente_possiede_badge_utente")
data class UtentePossiedeBadgeUtente(
    @Embedded val utente: Utente,
    @Relation(
        parentColumn="ID",
        entityColumn = "COD_BU",
        associateBy = Junction(UtenteBadgeUtenteCrossRef::class)
    )
    @ColumnInfo(name="badge_utenti") val badgeUtenti: List<BadgeUtente>,

    @ColumnInfo(name="data_acquisizione") val dataAcquisizione: Date,

    val esperienza : Int
)
@Entity(primaryKeys = ["ID","COD_BU"])
data class UtenteBadgeUtenteCrossRef (
    val ID : Int,
    val COD_BU : Int
)