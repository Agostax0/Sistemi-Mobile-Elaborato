package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.app.data.LocationDetails
import com.example.app.data.entity.Ristorante

@Dao
interface RistoranteDao{

    @Query("SELECT * FROM ristorante")
    fun getRistoranti() : List<Ristorante>

    //TODO sistemare
    @Query("SELECT * FROM ristorante " +
            "WHERE ristorante.posizione - :location BETWEEN 0 AND :distance")
    fun getRistorantiNearby(location: LocationDetails, distance: Int)

    //TODO aggiungere query quante persone hanno messo un ristorante tra i preferiti?
}