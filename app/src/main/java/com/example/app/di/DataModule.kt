package com.example.app.di

import android.content.Context
import com.example.app.FoodApp
import com.example.app.data.FoodAppDB
import com.example.app.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context) = SettingsRepository(context)
    @Singleton
    @Provides
    fun provideUtenteRepository(@ApplicationContext context: Context) =
        UtenteRepository((context.applicationContext as FoodApp).database.utenteDAO())
    @Singleton
    @Provides
    fun provideRistoranteRepository(@ApplicationContext context: Context) =
        RistoranteRepository((context.applicationContext as FoodApp).database.ristoranteDao())
    @Singleton
    @Provides
    fun provideCiboRepository(@ApplicationContext context: Context) =
        CiboRepository((context.applicationContext as FoodApp).database.ciboDAO())
    @Singleton
    @Provides
    fun provideTipoRistoranteRepository(@ApplicationContext context: Context) =
        TipoRistoranteRepository((context.applicationContext as FoodApp).database.tipoRistoranteDAO())
    @Singleton
    @Provides
    fun provideFiltroConsegnaRepository(@ApplicationContext context: Context) =
        FiltroConsegnaRepository((context.applicationContext as FoodApp).database.filtroConsegnaDAO(), context)
    @Singleton
    @Provides
    fun provideBadgeRistoranteRepository(@ApplicationContext context: Context) =
        BadgeRistoranteRepository((context.applicationContext as FoodApp).database.badgeRistoranteDAO())

    @Singleton
    @Provides
    fun provideBadgeUtenteRepository(@ApplicationContext context: Context) =
        BadgeUtenteRepository((context.applicationContext as FoodApp).database.badgeUtenteDAO())
    @Singleton
    @Provides
    fun provideRistoranteFiltroConsegnaRepository(@ApplicationContext context: Context) =
        RistoranteFiltroConsegnaRepository((context.applicationContext as FoodApp).database.ristoranteFiltroConsegnaDAO())
    @Singleton
    @Provides
    fun provideRistoranteTipoRepository(@ApplicationContext context: Context) =
        RistoranteTipoRistoranteRepository((context.applicationContext as FoodApp).database.ristoranteTipoRistoRanteDAO())
    @Singleton
    @Provides
    fun provideRistoranteMenuRistoranteRepository(@ApplicationContext context: Context) =
        RistoranteMenuRistoranteRepository((context.applicationContext as FoodApp).database.ristoranteMenuRistoranteDAO())
    @Singleton
    @Provides
    fun provideUtentePossiedBadgeRistoranteRepository(@ApplicationContext context: Context) =
        UtentePossiedBadgeRistoranteRepository((context.applicationContext as FoodApp).database.utentePossiedeBadgeRistoranteDao())
    @Singleton
    @Provides
    fun provideUtentePossiedeBadgeUtenteRepository(@ApplicationContext context: Context) =
        UtentePossiedeBadgeUtenteRepository((context.applicationContext as FoodApp).database.utentePossiedeBadgeUtenteDao())
    @Singleton
    @Provides
    fun provideUtenteScansionaRistoranteRepository(@ApplicationContext context: Context) =
        UtenteScansionaRistoranteRepository((context.applicationContext as FoodApp).database.utenteScansionaRistoranteDao())
}