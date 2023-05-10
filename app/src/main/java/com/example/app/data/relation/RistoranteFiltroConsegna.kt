package com.example.app.data.relation

import androidx.room.*
import com.example.app.data.entity.FiltroConsegna
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.TipoRistorante

@Entity(primaryKeys = ["COD_RIS","filtro"],
    foreignKeys = [
        ForeignKey(
            entity = Ristorante::class,
            parentColumns = ["COD_RIS"],
            childColumns = ["COD_RIS"]
        ),
        ForeignKey(
            entity = FiltroConsegna::class,
            parentColumns = ["filtro"],
            childColumns = ["filtro"]
        )
    ])
data class RistoranteFiltroConsegnaCrossRef (
    val COD_RIS : Int,
    val filtro : String
)

data class RistoranteFiltroConsegna(
    @Embedded val ristorante: Ristorante,
    @Relation(
        parentColumn="COD_RIS",
        entityColumn = "filtro",
        associateBy = Junction(RistoranteFiltroConsegnaCrossRef::class)
    )
    val filtri: List<FiltroConsegna>,
)