package com.example.app.data.relation

import androidx.room.*
import com.example.app.data.entity.BadgeUtente
import com.example.app.data.entity.Utente
import java.sql.Date

@Entity(primaryKeys = ["ID","COD_BU"],
    foreignKeys = [
        ForeignKey(
            entity = Utente::class,
            parentColumns = ["ID"],
            childColumns = ["ID"]
        ),
        ForeignKey(
            entity = BadgeUtente::class,
            parentColumns = ["COD_BU"],
            childColumns = ["COD_BU"]
        )
    ]
)
data class UtenteBadgeUtenteCrossRef (
    val ID : Int,
    val COD_BU : Int,
    @ColumnInfo(name="data_acquisizione") val dataAcquisizione: String,
    val esperienzaBadge : Int
)

data class UtentePossiedeBadgeUtente(
    @Embedded val utente: Utente,
    @Relation(
        parentColumn="ID",
        entityColumn = "COD_BU",
        associateBy = Junction(UtenteBadgeUtenteCrossRef::class)
    )
    val badgeUtenti: List<BadgeUtente>
)