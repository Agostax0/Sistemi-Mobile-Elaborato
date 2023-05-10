package com.example.app.data.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "utente", indices = [Index(value=["username","email"],unique=true)])
data class Utente (
    @PrimaryKey(autoGenerate = true)
    val ID : Int = 0,

    val nome: String,
    val cognome : String,
    val username : String,
    val password : String,
    val email : String,
    var icona : String,
    val esperienzaTotale : Long
)

