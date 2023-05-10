package com.example.app.data.relation

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.example.app.data.entity.BadgeRistorante
import com.example.app.data.entity.Utente
import java.sql.Date

@Entity(tableName = "utente_possiede_badge_ristorante")
data class UtentePossiedeBadgeRistorante(
    @Embedded val utente: Utente,
    @Relation(
        parentColumn="ID",
        entityColumn = "COD_BR",
        associateBy = Junction(UtenteBadgeRistoranteCrossRef::class)
    )
    @ColumnInfo(name="badge_ristoranti") val badgeRistoranti: List<BadgeRistorante>,

    @ColumnInfo(name="data_acquisizione") val dataAcquisizione: Date,

    val esperienza : Int
)

@Entity(primaryKeys = ["ID","COD_BR"])
data class UtenteBadgeRistoranteCrossRef (
    val ID : Int,
    val COD_BR : Int
    )