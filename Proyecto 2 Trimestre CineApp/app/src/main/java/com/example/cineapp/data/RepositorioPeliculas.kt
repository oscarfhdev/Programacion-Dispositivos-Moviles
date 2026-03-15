package com.example.cineapp.data

import kotlinx.coroutines.flow.Flow

class RepositorioPeliculas(private val peliculaDAO: PeliculaDAO) {

    fun obtenerPeliculas(): Flow<List<Pelicula>> = peliculaDAO.obtenerPeliculas()

    fun obtenerPelicula(id: Int): Flow<Pelicula?> = peliculaDAO.obtenerPelicula(id)

    suspend fun insertarPelicula(pelicula: Pelicula) {
        peliculaDAO.insertarPelicula(pelicula)
    }

    suspend fun actualizarPelicula(pelicula: Pelicula) {
        peliculaDAO.actualizarPelicula(pelicula)
    }

    suspend fun borrarPelicula(pelicula: Pelicula) {
        peliculaDAO.borrarPelicula(pelicula)
    }
}
