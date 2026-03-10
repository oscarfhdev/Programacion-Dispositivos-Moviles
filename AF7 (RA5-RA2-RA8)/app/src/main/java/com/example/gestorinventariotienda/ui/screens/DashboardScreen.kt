package com.example.gestorinventariotienda.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestorinventariotienda.ui.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    // Obtenemos los productos para procesar la información del gráfico
    val products by viewModel.productsFlow.collectAsState()

    // Calculamos los totales
    val totalProducts = products.size
    val lowStockProducts = products.count { it.quantity < 5 }
    val normalStockProducts = totalProducts - lowStockProducts

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Panel e Informes") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Visión Global del Inventario",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (totalProducts == 0) {
                Text("No hay datos para mostrar.")
            } else {
                // Dibujamos nuestro gráfico visual nativo usando Canvas
                DonutChart(
                    lowStockCount = lowStockProducts,
                    normalStockCount = normalStockProducts,
                    totalCount = totalProducts
                )

                Spacer(modifier = Modifier.height(32.dp))
                
                // Leyenda del Gráfico
                LegendItem(color = Color.Red, text = "Stock Bajo (<5): $lowStockProducts")
                Spacer(modifier = Modifier.height(8.dp))
                LegendItem(color = Color(0xFF4CAF50), text = "Stock Normal: $normalStockProducts")
            }
        }
    }
}

// Dibuja un gráfico de Donut proporciones calculadas a mano
@Composable
fun DonutChart(
    lowStockCount: Int,
    normalStockCount: Int,
    totalCount: Int
) {
    val lowStockAngle = (lowStockCount.toFloat() / totalCount.toFloat()) * 360f
    val normalStockAngle = 360f - lowStockAngle

    // Lienzo nativo de Compose
    Canvas(modifier = Modifier.size(200.dp)) {
        // Obtenemos el tamaño real para ajustar el trazo
        val strokeWidth = 40.dp.toPx()
        val size = Size(size.width - strokeWidth, size.height - strokeWidth)
        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
        
        // Arco 1: Productos con bajo Stock (Rojo)
        drawArc(
            color = Color.Red,
            startAngle = -90f,
            sweepAngle = lowStockAngle,
            useCenter = false, // false porque es un donut, no un trozo de pastel macizo
            topLeft = topLeft,
            size = size,
            style = Stroke(width = strokeWidth)
        )

        // Arco 2: Productos con Stock Normal (Verde)
        drawArc(
            color = Color(0xFF4CAF50),
            startAngle = -90f + lowStockAngle,
            sweepAngle = normalStockAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = strokeWidth)
        )
    }
}

// Representación visual de un punto de la leyenda
@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
