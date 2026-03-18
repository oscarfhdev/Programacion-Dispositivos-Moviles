package com.example.gestorinventariotienda.ui.viewmodels

import com.example.gestorinventariotienda.data.local.ProductEntity
import com.example.gestorinventariotienda.data.preferences.SettingsManager
import com.example.gestorinventariotienda.domain.repository.InventoryRepository
import com.example.gestorinventariotienda.utils.NetworkMonitor
import com.example.gestorinventariotienda.utils.NotificationHelper
import com.example.gestorinventariotienda.utils.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class FakeInventoryRepository : InventoryRepository {
    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())
    
    override fun getAllProducts(): Flow<List<ProductEntity>> = _products
    override suspend fun syncProductsFromApi() {}
    
    override suspend fun updateProductStock(product: ProductEntity) {
        val currentList = _products.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == product.id }
        if (index != -1) {
            currentList[index] = product
        } else {
            currentList.add(product)
        }
        _products.value = currentList
    }
    
    override suspend fun deleteProduct(product: ProductEntity) {}
    
    override suspend fun reportIncident(description: String) {}
    
    override fun getAllIncidents(): Flow<List<com.example.gestorinventariotienda.data.local.IncidentReportEntity>> = kotlinx.coroutines.flow.flowOf(emptyList())
}

@OptIn(ExperimentalCoroutinesApi::class)
class InventoryViewModelTest {

    private lateinit var viewModel: InventoryViewModel
    private lateinit var fakeRepository: FakeInventoryRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Enlazar el flujo general de UI Testing con nuestro dispatcher experimental asincrónico
        Dispatchers.setMain(testDispatcher)
        
        fakeRepository = FakeInventoryRepository()
        
        // Falsificar los recursos de Contexto nativo con Mockito para aislar el test unitario a la pura lógica MVVM
        val mockSettings = mock(SettingsManager::class.java)
        val mockNetwork = mock(NetworkMonitor::class.java)
        val mockPdfGen = mock(PdfGenerator::class.java)
        val mockNotification = mock(NotificationHelper::class.java)

        viewModel = InventoryViewModel(
            repository = fakeRepository,
            settingsManager = mockSettings,
            networkMonitor = mockNetwork,
            pdfGenerator = mockPdfGen,
            notificationHelper = mockNotification
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun updateStock_emitsCorrectState() = runTest {
        val initialProduct = ProductEntity(
            id = 50,
            title = "Test Product",
            price = 19.99,
            category = "Test Cat",
            imageUrl = "",
            quantity = 10
        )
        // Simulamos que el repositorio tiene un dato inicial
        fakeRepository.updateProductStock(initialProduct)

        // Iniciamos un colector en segundo plano. Esto engaña al 'SharingStarted.WhileSubscribed'
        // del StateFlow del ViewModel haciéndole creer que la Vista se ha dibujado y está escuchando.
        val job = backgroundScope.launch(kotlinx.coroutines.test.UnconfinedTestDispatcher(testScheduler)) {
            viewModel.products.collect {}
        }

        // Hacemos que el usuario reduzca el stock artificialmente desde el ViewModel (Interacción MVI/MVVM)
        viewModel.updateStock(initialProduct, 4)
        
        // Empujamos el reloj virtual de corrutinas para que termine instantáneamente
        testDispatcher.scheduler.advanceUntilIdle()

        // Testamos si Flow/StateFlow recogió un estado modificado con el número nuevo de stock
        val updatedProducts = viewModel.products.value
        org.junit.Assert.assertEquals(1, updatedProducts.size)
        org.junit.Assert.assertEquals(4, updatedProducts[0].quantity)
        
        job.cancel()
    }
}
