package com.example.cineapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PeliculaDAO {
    @Query("SELECT * FROM peliculas_table ORDER BY titulo ASC")
    fun obtenerPeliculas(): Flow<List<Pelicula>>

    @Query("SELECT * FROM peliculas_table WHERE id = :id")
    fun obtenerPelicula(id: Int): Flow<Pelicula?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarPelicula(pelicula: Pelicula)

    @Update
    suspend fun actualizarPelicula(pelicula: Pelicula)

    @Delete
    suspend fun borrarPelicula(pelicula: Pelicula)
}
