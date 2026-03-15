package com.example.casasapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// las queries para jugar con la base de datos de sqlite
@Dao
interface CasaDAO {
    // pillamos todas de golpe
    @Query("SELECT * FROM casas")
    fun getAll(): Flow<List<Casa>>

    // pillamos una sola pasando el id
    @Query("SELECT * FROM casas WHERE id = :id")
    fun getById(id: Int): Flow<Casa?>

    // metemos una
    @Insert
    suspend fun insert(casa: Casa)

    // por si ponemos cosita de editar mas tarde
    @Update
    suspend fun updateCasa(casa: Casa)

    // a tomar viento fresco
    @Delete
    suspend fun deleteCasa(casa: Casa)
}
