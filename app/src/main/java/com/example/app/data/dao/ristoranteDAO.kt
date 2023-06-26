package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.app.data.LocationDetails
import com.example.app.data.entity.Ristorante
import kotlinx.coroutines.flow.Flow

@Dao
interface RistoranteDAO{

    @Query("SELECT * FROM ristorante")
    fun getRistoranti() : Flow<List<Ristorante>>

    @Query("UPDATE ristorante SET numero_preferiti=:newPref WHERE COD_RIS=:COD_RIS")
    suspend fun updatePreferiti(newPref: Int, COD_RIS: Int)

    /*
    @Query("SELECT * FROM ristorante " +
            "WHERE ristorante.posizione - :location BETWEEN 0 AND :distance")
    fun getRistorantiNearby(location: LocationDetails, distance: Int)*/

    //TODO aggiungere query quante persone hanno messo un ristorante tra i preferiti?
}