package com.example.app.data.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.example.app.data.entity.FiltroConsegna
import com.example.app.data.entity.Ristorante

@Entity(tableName = "filtro_consegna_ristorante")
data class RistoranteFiltroConsegna(
    @Embedded val ristorante: Ristorante,
    @Relation(
        parentColumn="COD_RIS",
        entityColumn = "filtro",
        associateBy = Junction(RistoranteFiltroConsegnaCrossRef::class)
    )
    val filtri: List<FiltroConsegna>,
)
@Entity(primaryKeys = ["COD_RIS","filtro"])
data class RistoranteFiltroConsegnaCrossRef (
    val COD_RIS : Int,
    val filtro : String
)