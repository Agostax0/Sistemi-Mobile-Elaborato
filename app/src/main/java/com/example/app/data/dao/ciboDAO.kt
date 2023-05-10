package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.app.data.entity.Cibo
import kotlinx.coroutines.flow.Flow

@Dao
interface CiboDAO{
    @Query(
        "SELECT * FROM cibo"
    )
    fun getCibi() : Flow<List<Cibo>>
    @Query(
        "SELECT * FROM cibo WHERE cibo.nome_cibo=:nomeCibo"
    )
    fun getCibo(nomeCibo: String) : Cibo
}