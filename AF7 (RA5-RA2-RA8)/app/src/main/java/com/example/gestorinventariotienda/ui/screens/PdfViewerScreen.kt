package com.example.gestorinventariotienda.ui.screens

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(fileName: String, onBack: () -> Unit) {
    val context = LocalContext.current
    
    // Lista de imágenes generadas a partir del PDF
    val pdfPages = remember { mutableStateOf<List<Bitmap>?>(null) }
    
    LaunchedEffect(fileName) {
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, fileName)
            if (!file.exists()) return@withContext

            val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(parcelFileDescriptor)
            
            val bitmaps = mutableListOf<Bitmap>()
            
            // Renderizamos cada página a un Bitmap en alta resolución
            val pageCount = pdfRenderer.pageCount
            for (i in 0 until pageCount) {
                val page = pdfRenderer.openPage(i)
                // Usamos un tamaño amplio para mantener calidad (escalado proporcional x2)
                val bitmap = Bitmap.createBitmap(
                    page.width * 2,
                    page.height * 2,
                    Bitmap.Config.ARGB_8888
                )
                // Rellenamos el fondo de blanco para no tener transparencias feas
                bitmap.eraseColor(android.graphics.Color.WHITE)
                
                // Pintamos la página en el bitmap
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmaps.add(bitmap)
                
                // Cerramos la página para liberar memoria
                page.close()
            }
            
            // Cerramos el renderizador al terminar todas las páginas
            pdfRenderer.close()
            parcelFileDescriptor.close()
            
            pdfPages.value = bitmaps
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(fileName, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.DarkGray)
        ) {
            val pages = pdfPages.value
            if (pages == null) {
                // Estado de carga mientras se generan los bitmaps
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
            } else {
                // Listado de las hojas del PDF renderizadas
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(pages.size) { index ->
                        val bitmap = pages[index]
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Página ${index + 1}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
                        )
                    }
                }
            }
        }
    }
}
