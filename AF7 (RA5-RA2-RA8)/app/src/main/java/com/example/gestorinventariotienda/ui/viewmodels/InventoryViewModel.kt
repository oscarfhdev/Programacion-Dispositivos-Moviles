package com.example.gestorinventariotienda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorinventariotienda.data.local.ProductEntity
import com.example.gestorinventariotienda.data.preferences.SettingsManager
import com.example.gestorinventariotienda.domain.repository.InventoryRepository
import com.example.gestorinventariotienda.utils.NetworkMonitor
import com.example.gestorinventariotienda.utils.NotificationHelper
import com.example.gestorinventariotienda.utils.PdfGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// Anotamos con @HiltViewModel para que Dagger Hilt inyecte todas las dependencias en el constructor.
@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: InventoryRepository,
    private val settingsManager: SettingsManager,
    private val networkMonitor: NetworkMonitor,
    private val pdfGenerator: PdfGenerator,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    // Exponemos la lista de productos obtenidos de Room directamente a la UI mediante un StateFlow.
    // Usamos stateIn para convertir el Flow de Room en un StateFlow que Jetpack Compose puede observar.
    val products: StateFlow<List<ProductEntity>> = repository.getAllProducts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Estado para mostrar mensajes o errores en la UI (por ejemplo en un Snackbar o Toast)
    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage

    // Estado para indicar si estamos descargando de la API
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun clearUiMessage() {
        _uiMessage.value = null
    }

    // Función para descargar los productos iniciales de FakeStore API
    fun downloadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Obtenemos el nombre y SSID WiFi de la tienda desde DataStore.
                val storeName = settingsManager.storeNameFlow.first()
                val requiredSsid = settingsManager.storeWifiSsidFlow.first()

                // Verificamos que el usuario haya configurado previamente la app
                if (storeName.isBlank() || requiredSsid.isBlank()) {
                    _uiMessage.value = "Error: Configura primero el Nombre de la Tienda y el SSID de la Wi-Fi en Ajustes."
                    return@launch
                }

                // 2. Verificamos la conexión
                if (!networkMonitor.isConnected()) {
                    _uiMessage.value = "Error: No hay conexión a Internet."
                    return@launch
                }

                // 3. Verificamos si la red WiFi es la correcta
                if (!networkMonitor.isConnectedToStoreWifi(requiredSsid)) {
                    _uiMessage.value = "Error: No estás conectado a la WiFi de la tienda ($requiredSsid)."
                    return@launch
                }

                // 4. Si pasamos los controles, sincronizamos
                _uiMessage.value = "Descargando catálogo..."
                repository.syncProductsFromApi()
                _uiMessage.value = "Catálogo descargado con éxito."
            } catch (e: Exception) {
                _uiMessage.value = "Error al descargar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para modificar la cantidad de stock de un producto localmente
    fun updateStock(product: ProductEntity, newQuantity: Int) {
        viewModelScope.launch {
            // Evitamos cantidades negativas
            val validQuantity = if (newQuantity < 0) 0 else newQuantity
            
            // Actualizamos en Room
            repository.updateProductStock(product.copy(quantity = validQuantity))

            // Comprobamos si hay que lanzar Notificación Local por stock bajo (< 5)
            if (validQuantity < 5) {
                notificationHelper.showLowStockNotification(product.title, validQuantity)
            }
        }
    }

    // Función principal para generar el PDF desde la UI
    fun exportInventoryToPdf() {
        viewModelScope.launch {
            val currentProducts = products.value
            val storeName = settingsManager.storeNameFlow.first()
            
            if (currentProducts.isEmpty()) {
                _uiMessage.value = "El inventario está vacío. No hay nada que exportar."
                return@launch
            }

            _uiMessage.value = "Generando PDF..."
            
            // Llamamos a nuestro generador
            val result = pdfGenerator.generateInventoryPdf(currentProducts, storeName)
            
            result.onSuccess { file ->
                _uiMessage.value = "PDF guardado en: ${file.name}"
                // Lanzamos la notificación
                notificationHelper.showPdfGeneratedNotification(file.name)
            }.onFailure {
                _uiMessage.value = "Error al generar el PDF: ${it.message}"
            }
        }
    }

    // Función para borrar un producto del inventario local
    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            _uiMessage.value = "Producto eliminado: ${product.title}"
        }
    }
}
