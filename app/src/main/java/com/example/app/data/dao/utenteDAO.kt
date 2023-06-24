package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app.data.entity.Utente
import kotlinx.coroutines.flow.Flow

@Dao
interface UtenteDAO{
    @Query("SELECT * FROM utente")
    fun getUtenti(): Flow<List<Utente>>

    @Query("SELECT * FROM utente WHERE username=:username AND password=:password")
    fun checkLoginCredentials(username: String,password:String) : Flow<Utente?>
    
    @Query("SELECT * FROM utente WHERE utente.username=:username")
    fun getUtenteFromUsername(username: String) : Utente

    @Query("UPDATE utente SET username=:newUsername WHERE utente.ID=:userId")
    suspend fun changeUsernameFromId(userId: Int, newUsername: String)

    @Query("UPDATE utente SET icona=:newIcona WHERE utente.ID=:userId")
    suspend fun changeIconaFromId(userId: Int, newIcona: String)

    @Query("UPDATE utente SET esperienzaTotale=:newExp WHERE utente.ID=:userId")
    suspend fun updateExpFromId(userId: Int, newExp: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg utente: Utente)
}