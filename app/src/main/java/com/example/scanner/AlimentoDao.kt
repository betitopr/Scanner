package com.example.scanner
import androidx.room.*

@Dao
interface AlimentoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlimento(alimento: Alimento)

    @Query("SELECT * FROM alimentos")
    suspend fun getAllAlimentos(): List<Alimento>
}