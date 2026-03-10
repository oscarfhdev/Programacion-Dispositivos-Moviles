package com.example.gestorinventariotienda.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestorinventariotienda.data.local.ProductEntity
import com.example.gestorinventariotienda.ui.viewmodels.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Inyectamos el ViewModel directamente desde la firma de la función gracias a Hilt
fun InventoryScreen(
    viewModel: InventoryViewModel = hiltViewModel()
) {
    // Escuchamos la lista de productos de Room
    val products by viewModel.products.collectAsState()
    // Escuchamos los mensajes de la UI para mostrarlos en Toasts
    val uiMessage by viewModel.uiMessage.collectAsState()
    
    val context = LocalContext.current

    // Efecto secundario que lanza un Toast cada vez que el ViewModel lo solicita
    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearUiMessage() // Limpiamos para que no se repita
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario Tienda") },
                actions = {
                    // Botón en la Toolbar para bajarnos los productos via Retrofit
                    IconButton(onClick = { viewModel.downloadProducts() }) {
                        Icon(Icons.Default.Download, contentDescription = "Sincronizar API")
                    }
                }
            )
        },
        floatingActionButton = {
            // FAB Flotante para exportar el inventario actual a PDF
            FloatingActionButton(onClick = { viewModel.exportInventoryToPdf() }) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = "Exportar PDF")
            }
        }
    ) { paddingValues ->
        // Contenedor principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (products.isEmpty()) {
                // Mensaje si no hay productos
                Text(
                    text = "No hay productos. Pulsa sincronizar para descargar el catálogo.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                // Lista scrolleable de productos
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductItem(
                            product = product,
                            onStockUpdate = { newStock ->
                                // Llamamos a actualizar dentro de Room
                                viewModel.updateStock(product, newStock)
                            }
                        )
                    }
                }
            }
        }
    }
}

// Componente visual (Item) para mostrar cada producto en la lista
@Composable
fun ProductItem(
    product: ProductEntity,
    onStockUpdate: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Si hay poco stock ponemos el fondo rojizo como alerta visual
        colors = CardDefaults.cardColors(
            containerColor = if (product.quantity < 5) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Título truncado si es muy largo
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = "Categoria: ${product.category}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Precio: \$${product.price}", style = MaterialTheme.typography.bodyMedium)
                
                if (product.quantity < 5) {
                    Text(text = "¡Stock bajo!", color = Color.Red, style = MaterialTheme.typography.labelSmall)
                }
            }
            
            // Controles para subir y bajar stock a mano
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { onStockUpdate(product.quantity - 1) }) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${product.quantity}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onStockUpdate(product.quantity + 1) }) {
                    Text("+")
                }
            }
        }
    }
}
