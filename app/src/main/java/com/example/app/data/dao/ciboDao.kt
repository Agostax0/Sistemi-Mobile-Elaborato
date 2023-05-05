package com.example.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.app.data.entity.Cibo

@Dao
interface ciboDao{
    @Query(
        "SELECT * FROM cibo WHERE cibo.nome_cibo=:nomeCibo"
    )
    fun getCibo(nomeCibo: String) : Cibo
}