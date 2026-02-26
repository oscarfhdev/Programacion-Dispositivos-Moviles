package com.example.casasapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object para la entidad Casa.
 * Room genera la implementación de esta interfaz en tiempo de compilación.
 */
@Dao
interface CasaDAO {
    /** Obtiene todas las casas como un Flow reactivo */
    @Query("SELECT * FROM casas")
    fun getAll(): Flow<List<Casa>>

    /** Busca una casa por su ID */
    @Query("SELECT * FROM casas WHERE id = :id")
    fun getById(id: Int): Flow<Casa?>

    /** Inserta una nueva casa */
    @Insert
    suspend fun insert(casa: Casa)

    /** Actualiza una casa existente */
    @Update
    suspend fun update(casa: Casa)

    /** Elimina una casa */
    @Delete
    suspend fun delete(casa: Casa)
}
