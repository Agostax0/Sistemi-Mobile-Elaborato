package com.example.app.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.app.data.LocationDetails

@Entity(tableName = "ristorante")
data class Ristorante(
    @PrimaryKey(autoGenerate = true)
    val COD_RIS : Int = 0,

    val COD_BR : Int,

    val nome: String,

    var icona : String,

    @ColumnInfo(name= "numero_preferiti") val numeroPreferiti: Long,

    val orari: String,

    val posizione : String,

    @ColumnInfo(name= "numero_telefono", defaultValue = "1234567890") val numeroTelefono: String,

    @ColumnInfo(defaultValue = "descrizione")val descrizione: String
)
