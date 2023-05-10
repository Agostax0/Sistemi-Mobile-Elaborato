package com.example.app.data.relation

import androidx.room.*
import com.example.app.data.entity.BadgeUtente
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.TipoRistorante
import com.example.app.data.entity.Utente

@Entity(primaryKeys = ["COD_RIS","nome_tipo"],
    foreignKeys = [
        ForeignKey(
            entity = Ristorante::class,
            parentColumns = ["COD_RIS"],
            childColumns = ["COD_RIS"]
        ),
        ForeignKey(
            entity = TipoRistorante::class,
            parentColumns = ["nome_tipo"],
            childColumns = ["nome_tipo"]
        )
    ]
)
data class RistoranteTipoRistoranteCrossRef (
    val COD_RIS : Int,
    val nome_tipo : String
)

data class RistoranteTipoRistorante(
    @Embedded val ristorante: Ristorante,
    @Relation(
        parentColumn="COD_RIS",
        entityColumn = "nome_tipo",
        associateBy = Junction(RistoranteTipoRistoranteCrossRef::class)
    )
    val tipi: List<TipoRistorante>
)