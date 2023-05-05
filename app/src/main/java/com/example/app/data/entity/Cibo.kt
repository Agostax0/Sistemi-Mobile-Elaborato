package com.example.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cibo")
data class Cibo(
    @PrimaryKey
    @ColumnInfo(name="nome_cibo") val nomeCibo: String,

    val gruppo: String,

    @ColumnInfo(name="flag_offerta") val flagOfferta : Boolean
)