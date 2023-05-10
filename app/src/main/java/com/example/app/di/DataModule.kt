package com.example.app.di

import android.content.Context
import com.example.app.FoodApp
import com.example.app.data.FoodAppDB
import com.example.app.data.repository.UtenteRepository
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
    fun provideFoodRepository(@ApplicationContext context: Context) =
        UtenteRepository((context.applicationContext as FoodApp).database.utenteDAO())
}