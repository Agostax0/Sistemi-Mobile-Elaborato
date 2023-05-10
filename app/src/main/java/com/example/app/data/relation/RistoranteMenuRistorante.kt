package com.example.app.data.relation

import androidx.room.*
import com.example.app.data.entity.Cibo
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.TipoRistorante

@Entity(primaryKeys = ["COD_RIS","nome_cibo"],
    foreignKeys = [
        ForeignKey(
            entity = Ristorante::class,
            parentColumns = ["COD_RIS"],
            childColumns = ["COD_RIS"]
        ),
        ForeignKey(
            entity = Cibo::class,
            parentColumns = ["nome_cibo"],
            childColumns = ["nome_cibo"]
        )
    ])
data class RistoranteMenuRistoranteCrossRef (
    val COD_RIS : Int,
    val nome_cibo : String
)

data class RistoranteMenuRistorante(
    @Embedded val ristorante: Ristorante,
    @Relation(
        parentColumn="COD_RIS",
        entityColumn = "nome_cibo",
        associateBy = Junction(RistoranteMenuRistoranteCrossRef::class)
    )
    val menu: List<Cibo>,
)