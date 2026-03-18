package com.example.gestorinventariotienda.data.repository

import com.example.gestorinventariotienda.data.local.IncidentDao
import com.example.gestorinventariotienda.data.local.IncidentReportEntity
import com.example.gestorinventariotienda.data.local.ProductDao
import com.example.gestorinventariotienda.data.local.ProductEntity
import com.example.gestorinventariotienda.data.remote.FakeStoreApi
import com.example.gestorinventariotienda.domain.repository.InventoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Implementación real del repositorio.
// Usamos @Inject constructor para que Dagger Hilt sepa cómo crear esta clase, y le pasamos 
// las dependencias que necesita (Room Daos y Retrofit Api).
class InventoryRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val incidentDao: IncidentDao,
    private val api: FakeStoreApi
) : InventoryRepository {

    // Simplemente llamamos al DAO para que nos devuelva la lista observable de productos.
    override fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    // Lógica principal: Sincronizar desde la web.
    override suspend fun syncProductsFromApi() {
        try {
            // 1. Descargamos la lista de la API.
            val remoteProducts = api.getProducts()
            
            // 2. Mapeamos los DTOs que vienen de la red a nuestras entidades locales (Entity).
            // Le ponemos un stock inicial de 0, ya que estamos inicializando el inventario.
            val entities = remoteProducts.map { dto ->
                ProductEntity(
                    id = dto.id,
                    title = dto.title,
                    price = dto.price,
                    quantity = 0, 
                    category = dto.category,
                    imageUrl = dto.image
                )
            }
            // 3. Limpiamos la base de datos antigua e insertamos la nueva remesa.
            productDao.clearAllProducts()
            productDao.insertProducts(entities)
            
        } catch (e: Exception) {
            // Aquí atraparíamos errores de red, servidor caído, etc.
            e.printStackTrace()
        }
    }

    // Actualizamos un producto en Room (por ejemplo, sumamos o restamos stock).
    override suspend fun updateProductStock(product: ProductEntity) {
        productDao.updateProduct(product)
    }

    // Borramos un producto de Room.
    override suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    // Creamos y guardamos una nueva incidencia.
    override suspend fun reportIncident(description: String) {
        val incident = IncidentReportEntity(description = description)
        incidentDao.insertIncident(incident)
    }

    // Obtenemos todas las incidencias (Flow).
    override fun getAllIncidents(): Flow<List<IncidentReportEntity>> {
        return incidentDao.getAllIncidents()
    }
}
