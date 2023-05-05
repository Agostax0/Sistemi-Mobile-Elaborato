package com.example.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filtro_consegna")
data class FiltroConsegna(
    @PrimaryKey
    val filtro: String
)