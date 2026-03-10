package com.example.gestorinventariotienda.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Definimos esta clase como una entidad de Room (una tabla en la base de datos).
// Le damos el nombre a la tabla 'products'
@Entity(tableName = "products")
data class ProductEntity(
    // Añadimos la clave primaria. Usaremos el ID que viene de la API, por lo que no es autogenerado.
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    // Añadimos una cantidad por defecto para el inventario, ya que la API solo trae productos,
    // pero nosotros queremos gestionar su stock en la tienda (ej. por defecto 0 al descargar).
    val quantity: Int = 0,
    val category: String,
    val imageUrl: String
)
