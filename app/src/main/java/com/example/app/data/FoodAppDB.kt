package com.example.app.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.app.data.dao.*
import com.example.app.data.entity.*
import com.example.app.data.relation.*

@Database(entities = [
    Cibo::class, Ristorante::class, FiltroConsegna::class, Utente::class, BadgeRistorante::class, BadgeUtente::class, TipoRistorante::class,
    RistoranteTipoRistoranteCrossRef::class, RistoranteMenuRistoranteCrossRef::class, RistoranteFiltroConsegnaCrossRef::class, UtenteRistoranteCrossRef::class,
    UtenteBadgeUtenteCrossRef::class, UtenteBadgeRistoranteCrossRef::class], version = 2, exportSchema = true, autoMigrations = [AutoMigration (from = 1, to = 2)])
abstract class FoodAppDB : RoomDatabase() {

    abstract fun utenteDAO(): UtenteDAO
    abstract fun ristoranteDao(): RistoranteDAO
    abstract fun badgeRistoranteDAO(): BadgeRistoranteDAO
    abstract fun badgeUtenteDAO(): BadgeUtenteDAO
    abstract fun filtroConsegnaDAO(): FiltroConsegnaDAO
    abstract fun ciboDAO(): CiboDAO
    abstract fun tipoRistoranteDAO(): TipoRistoranteDAO
    abstract fun ristoranteFiltroConsegnaDAO(): RistoranteFiltroConsegnaDAO
    abstract fun ristoranteMenuRistoranteDAO(): RistoranteMenuRistoranteDAO
    abstract fun ristoranteTipoRistoRanteDAO(): RistoranteTipoRistoranteDAO
    abstract fun utentePossiedeBadgeRistoranteDao(): UtentePossiedeBadgeRistoranteDAO
    abstract fun utentePossiedeBadgeUtenteDao(): UtentePossiedeBadgeUtenteDAO
    abstract fun utenteScansionaRistoranteDao(): UtenteScansionaRistoranteDAO

    companion object {
        @Volatile
        private var INSTANCE: FoodAppDB ?= null

        fun getDatabase(context: Context): FoodAppDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodAppDB::class.java,
                    "food_database"
                )//.createFromAsset("database/food.db")
                .build()
                INSTANCE = instance

                instance
            }
        }
    }

}