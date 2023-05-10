package com.example.app.data.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.Utente

@Entity(tableName = "scansiona")
data class UtenteScansionaRistorante(
    @Embedded val utente: Utente,
    @Relation(
        parentColumn="ID",
        entityColumn = "COD_RIS",
        associateBy = Junction(UtenteRistoranteCrossRef::class)
    )
    val ristoranti: List<Ristorante>,

    val preferito: Boolean
    )
@Entity(primaryKeys = ["ID","COD_RIS"])
data class UtenteRistoranteCrossRef (
    val ID : Int,
    val COD_RIS : Int
)