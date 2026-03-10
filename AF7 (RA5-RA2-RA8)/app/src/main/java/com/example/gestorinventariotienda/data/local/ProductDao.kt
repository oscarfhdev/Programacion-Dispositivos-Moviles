package com.example.gestorinventariotienda.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Objeto de acceso a datos (DAO) para interactuar con la tabla de productos.
@Dao
interface ProductDao {
    
    // Obtenemos todos los productos. Devolvemos un Flow para que la UI se actualice
    // automáticamente cuando hayan cambios en la tabla.
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    // Insertamos una lista de productos. Si ya existe uno con el mismo ID, lo reemplazamos.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)
    
    // Permite actualizar un producto en particular (por ejemplo, al cambiar su stock desde la UI).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProduct(product: ProductEntity)

    // Borramos todos los productos para poder recargar desde la API de cero si la tienda lo requiere.
    @Query("DELETE FROM products")
    suspend fun clearAllProducts()
}
