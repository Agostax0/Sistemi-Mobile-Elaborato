package com.example.app.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "tipo_ristorante")
data class TipoRistorante(
    @PrimaryKey
    @ColumnInfo(name="nome_tipo") val nomeTipo: String,

    @Ignore
    var icona: Bitmap
)