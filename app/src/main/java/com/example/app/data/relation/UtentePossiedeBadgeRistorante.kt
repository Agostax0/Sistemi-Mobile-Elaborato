package com.example.app.data.relation

import androidx.room.*
import com.example.app.data.entity.BadgeRistorante
import com.example.app.data.entity.BadgeUtente
import com.example.app.data.entity.Utente
import java.sql.Date

@Entity(primaryKeys = ["ID","COD_BR"],
    foreignKeys = [
        ForeignKey(
            entity = Utente::class,
            parentColumns = ["ID"],
            childColumns = ["ID"]
        ),
        ForeignKey(
            entity = BadgeRistorante::class,
            parentColumns = ["COD_BR"],
            childColumns = ["COD_BR"]
        )
    ]
)
data class UtenteBadgeRistoranteCrossRef (
    val ID : Int,
    val COD_BR : Int,
    @ColumnInfo(name="data_acquisizione") val dataAcquisizione: String,
    val esperienzaBadge : Int
)

data class UtentePossiedeBadgeRistorante(
    @Embedded val utente: Utente,
    @Relation(
        parentColumn="ID",
        entityColumn = "COD_BR",
        associateBy = Junction(UtenteBadgeRistoranteCrossRef::class)
    )
    val badgeRistoranti: List<BadgeRistorante>
)