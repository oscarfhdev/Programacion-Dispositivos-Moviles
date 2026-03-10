package com.example.gestorinventariotienda.domain.repository

import com.example.gestorinventariotienda.data.local.IncidentReportEntity
import com.example.gestorinventariotienda.data.local.ProductEntity
import kotlinx.coroutines.flow.Flow

// Definimos la interfaz (contrato) de nuestro repositorio.
// La capa de UI (ViewModels) solo conocerá esta interfaz, sin importarle si los datos
// vienen de Room, Retrofit o si estamos simulando datos.
interface InventoryRepository {
    
    // Obtenemos todos los productos como un flujo constante de datos.
    fun getAllProducts(): Flow<List<ProductEntity>>
    
    // Descargamos los productos desde la API (FakeStore) y los guardamos en la base de datos local.
    suspend fun syncProductsFromApi()

    // Actualizamos la cantidad de stock de un producto específico.
    suspend fun updateProductStock(product: ProductEntity)

    // Agregamos una nueva incidencia al buzón.
    suspend fun reportIncident(description: String)

    // Obtenemos el listado de todas las incidencias registradas.
    fun getAllIncidents(): Flow<List<IncidentReportEntity>>
}
