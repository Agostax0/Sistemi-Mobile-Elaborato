package com.example.app.data.relation

import androidx.room.*
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.Utente

@Entity(primaryKeys = ["ID","COD_RIS"],
    foreignKeys = [
        ForeignKey(
            entity = Utente::class,
            parentColumns = ["ID"],
            childColumns = ["ID"]
        ),
        ForeignKey(
            entity = Ristorante::class,
            parentColumns = ["COD_RIS"],
            childColumns = ["COD_RIS"]
        )
    ]
)
data class UtenteRistoranteCrossRef (
    val ID : Int,
    val COD_RIS : Int,
    val preferito: Boolean
)

data class UtenteScansionaRistorante(
    @Embedded val utente: Utente,
    @Relation(
        parentColumn="ID",
        entityColumn = "COD_RIS",
        associateBy = Junction(UtenteRistoranteCrossRef::class)
    )
    val ristoranti: List<Ristorante>
)
