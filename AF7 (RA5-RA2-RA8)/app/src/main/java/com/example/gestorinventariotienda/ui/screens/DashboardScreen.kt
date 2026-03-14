package com.example.gestorinventariotienda.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
            CenterAlignedTopAppBar(title = { Text("Panel e Informes", fontWeight = FontWeight.Bold) })
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
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(48.dp))

            if (totalProducts == 0) {
                Text("No hay datos para mostrar.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                // Dibujamos nuestro gráfico visual nativo usando Canvas animado
                DonutChart(
                    lowStockCount = lowStockProducts,
                    normalStockCount = normalStockProducts,
                    totalCount = totalProducts
                )

                Spacer(modifier = Modifier.height(48.dp))
                
                // Leyenda del Gráfico
                LegendItem(color = Color(0xFFE53935), text = "Stock Bajo (<5): $lowStockProducts")
                Spacer(modifier = Modifier.height(16.dp))
                LegendItem(color = Color(0xFF43A047), text = "Stock Normal: $normalStockProducts")
            }
        }
    }
}

// Dibuja un gráfico de Donut con transiciones animadas proporcionales
@Composable
fun DonutChart(
    lowStockCount: Int,
    normalStockCount: Int,
    totalCount: Int
) {
    val lowStockAngle = (lowStockCount.toFloat() / totalCount.toFloat()) * 360f
    val normalStockAngle = 360f - lowStockAngle

    // Estado para disparar la animación al entrar
    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    // Animaciones suaves y progresivas
    val currentLowStockAngle by animateFloatAsState(
        targetValue = if (animationPlayed) lowStockAngle else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "lowStockAnim"
    )
    val currentNormalStockAngle by animateFloatAsState(
        targetValue = if (animationPlayed) normalStockAngle else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "normalStockAnim"
    )

    // Lienzo nativo de Compose
    Canvas(modifier = Modifier.size(240.dp)) {
        // Obtenemos el tamaño real para ajustar el trazo
        val strokeWidth = 50.dp.toPx()
        val size = Size(size.width - strokeWidth, size.height - strokeWidth)
        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
        
        // Arco 1: Productos con bajo Stock (Rojo)
        drawArc(
            color = Color(0xFFE53935), // Rojo más material
            startAngle = -90f,
            sweepAngle = currentLowStockAngle,
            useCenter = false, // false porque es un donut, no un pastel macizo
            topLeft = topLeft,
            size = size,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )

        // Arco 2: Productos con Stock Normal (Verde)
        drawArc(
            color = Color(0xFF43A047), // Verde más material
            startAngle = -90f + currentLowStockAngle,
            sweepAngle = currentNormalStockAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )
    }
}

// Representación visual de un punto de la leyenda
@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
    ) {
        Canvas(modifier = Modifier.size(20.dp)) {
            drawCircle(color = color)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
}
