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

    /**
     * Ritorna 1 (esiste) o 0 in base se le credenziali utente sono corrette
     */
    @Query("SELECT COUNT(*) FROM utente WHERE " +
            "(utente.email = :userEmail AND utente.password = :userPassword)  OR " +
            "(utente.username = :userPassword AND utente.password = :userPassword )")
    fun checkLogin(userEmail: String, userUsername: String, userPassword: String) : Int

    @Query("UPDATE utente SET username = :newUsername WHERE utente.ID = :userId")
    suspend fun changeUsernameFromId(userId: Int, newUsername: String)

    //altrimenti https://developer.android.com/reference/kotlin/androidx/room/Update
    @Update(entity = Utente::class)
    suspend fun changeUsername(vararg userUsername: String)

    //TODO update icona

    //TODO update esperienza

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg utente: Utente)
}