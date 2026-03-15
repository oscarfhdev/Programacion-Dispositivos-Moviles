package com.example.casasapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This data class represents a house in the application. 
 * The @Entity annotation marks it as a table in the Room database.
 */
@Entity(tableName = "casas")
data class Casa(
    /**
     * The @PrimaryKey annotation marks this field as the primary key of the table.
     * The autoGenerate = true parameter tells Room to automatically generate a unique ID for each new house.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val imagen: String, // URI of the image
    val descripcion: String,
    val habitaciones: Int,
    val banos: Int,
    val tieneTerreno: Boolean,
    val tienePiscina: Boolean,
    val tieneGaraje: Boolean
)
