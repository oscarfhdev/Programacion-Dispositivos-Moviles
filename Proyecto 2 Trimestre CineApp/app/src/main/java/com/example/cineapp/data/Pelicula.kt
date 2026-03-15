package com.example.cineapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "peliculas_table")
data class Pelicula(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val imagen: String, // URI string
    val duracionMinutos: Int,
    val añoLanzamiento: Int,
    val estaAlquilada: Boolean,
    val formatoFisico: Boolean
)
