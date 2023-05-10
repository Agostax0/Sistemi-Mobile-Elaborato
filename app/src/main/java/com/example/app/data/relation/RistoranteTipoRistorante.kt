package com.example.app.data.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.example.app.data.entity.Ristorante
import com.example.app.data.entity.TipoRistorante

@Entity(tableName = "tipo")
data class RistoranteTipoRistorante(
    @Embedded val ristorante: Ristorante,
    @Relation(
        parentColumn="COD_RIS",
        entityColumn = "nome_tipo",
        associateBy = Junction(RistoranteTipoRistoranteCrossRef::class)
    )
    val tipi: List<TipoRistorante>,
)
@Entity(primaryKeys = ["COD_RIS","nome_tipo"])
data class RistoranteTipoRistoranteCrossRef (
    val COD_RIS : Int,
    val nomeTipo : String
)