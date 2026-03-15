package com.example.cineapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineapp.data.Pelicula
import com.example.cineapp.data.RepositorioPeliculas
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CineViewModel(private val repositorioPeliculas: RepositorioPeliculas) : ViewModel() {

    // Estado principal de la lista de películas para la Galería
    val peliculas: StateFlow<List<Pelicula>> = repositorioPeliculas.obtenerPeliculas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun guardarPelicula(pelicula: Pelicula) {
        viewModelScope.launch {
            repositorioPeliculas.insertarPelicula(pelicula)
        }
    }

    fun actualizarPelicula(pelicula: Pelicula) {
        viewModelScope.launch {
            repositorioPeliculas.actualizarPelicula(pelicula)
        }
    }

    fun borrarPelicula(pelicula: Pelicula) {
        viewModelScope.launch {
            repositorioPeliculas.borrarPelicula(pelicula)
        }
    }

    // Devuelve el Flow de una película en específico (para Detalles o Edición)
    fun obtenerPelicula(id: Int) = repositorioPeliculas.obtenerPelicula(id)
}
