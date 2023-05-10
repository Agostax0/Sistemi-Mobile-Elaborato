package com.example.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app.data.dao.*
import com.example.app.data.entity.*

@Database(entities = [
    Cibo::class, Ristorante::class, FiltroConsegna::class, Utente::class, BadgeRistorante::class, BadgeUtente::class, TipoRistorante::class
                     ], version = 1, exportSchema = true)
abstract class FoodAppDB : RoomDatabase() {

    abstract fun utenteDAO(): UtenteDao
    abstract fun ristoranteDao(): RistoranteDao
    abstract fun badgeRistoranteDAO(): BadgeRistoranteDao
    abstract fun badgeUtenteDAO(): BadgeUtenteDao
    abstract fun filtroConsegnaDAO(): FiltroConsegnaDao
    abstract fun ciboDAO(): CiboDao
    abstract fun ristoranteFiltroConsegnaDAO(): RistoranteFiltroConsegnaDao
    abstract fun ristoranteMenuRistoranteDAO(): RistoranteMenuRistoranteDao
    abstract fun ristoranteTipoRistoranteDAO(): RistoranteTipoRistoranteDao
    abstract fun utentePossiedeBadgeRistoranteDao(): UtentePossiedeBadgeRistoranteDao
    abstract fun utentePossiedeBadgeUtenteDao(): UtentePossiedeBadgeUtenteDao
    abstract fun utenteScansionaRistoranteDao(): UtenteScansionaRistoranteDao


    companion object {
        @Volatile
        private var INSTANCE: FoodAppDB ?= null

        fun getDatabase(context: Context): FoodAppDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodAppDB::class.java,
                    "items_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }
    }

}