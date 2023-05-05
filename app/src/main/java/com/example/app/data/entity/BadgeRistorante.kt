package com.example.app.data.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "badge_ristorante")
data class BadgeRistorante(
    @PrimaryKey(autoGenerate = true)
    val COD_BR : Int = 0
)