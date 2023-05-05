package com.example.app.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(tableName = "menu_ristorante")
data class RistoranteMenuRistorante(
    @Embedded val ristorante: Ristorante,
    @Relation(
        parentColumn="COD_RIS",
        entityColumn = "cibo",
        associateBy = Junction(RistoranteMenuRistoranteCrossRef::class)
    )
    val menu: List<Cibo>,
)
@Entity(primaryKeys = ["COD_RIS","cibo"])
data class RistoranteMenuRistoranteCrossRef (
    val COD_RIS : Int,
    val cibo : String
)