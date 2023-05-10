package com.example.app.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "badge_utente")
data class BadgeUtente(
    @PrimaryKey(autoGenerate = true)
    val COD_BU : Int = 0,

    val nome : String,

    val descrizione: String,

    val livello : Int,

    var icona : String,

    @ColumnInfo(name="numero_utenti") val numeroUtenti: Int
)