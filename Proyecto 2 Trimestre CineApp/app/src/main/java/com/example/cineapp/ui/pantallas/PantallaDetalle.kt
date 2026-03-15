package com.example.cineapp.ui.pantallas

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DiscFull
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cineapp.data.Pelicula
import com.example.cineapp.ui.viewmodel.CineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    viewModel: CineViewModel,
    peliculaId: Int,
    onVolver: () -> Unit
) {
    var pelicula by remember { mutableStateOf<Pelicula?>(null) }

    LaunchedEffect(peliculaId) {
        viewModel.obtenerPelicula(peliculaId).collect { it?.let { p -> pelicula = p } }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la Película") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        pelicula?.let { p ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen grande
                AsyncImage(
                    model = if (p.imagen.isNotEmpty()) Uri.parse(p.imagen) else null,
                    contentDescription = "Portada extendida de ${p.titulo}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop,
                    fallback = painterResource(id = android.R.drawable.ic_menu_gallery),
                    error = painterResource(id = android.R.drawable.ic_menu_report_image)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = p.titulo,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Chips o Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistChip(
                            onClick = { },
                            label = { Text("${p.duracionMinutos} min") },
                            leadingIcon = { Icon(Icons.Filled.AvTimer, contentDescription = "Duración") }
                        )

                        AssistChip(
                            onClick = { },
                            label = { Text(p.fechaLanzamiento) },
                            leadingIcon = { Icon(Icons.Filled.DateRange, contentDescription = "Fecha") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistChip(
                            onClick = { },
                            label = { Text(if (p.formatoFisico) "Físico" else "Digital") },
                            leadingIcon = { Icon(Icons.Filled.DiscFull, contentDescription = "Formato") }
                        )

                        AssistChip(
                            onClick = { },
                            label = { Text(if (p.estaAlquilada) "Alquilada" else "Disponible") },
                            leadingIcon = { Icon(Icons.Filled.CheckCircle, contentDescription = "Estado") },
                            colors = AssistChipDefaults.assistChipColors(
                                labelColor = if (p.estaAlquilada) MaterialTheme.colorScheme.error else Color(0xFF388E3C),
                                leadingIconContentColor = if (p.estaAlquilada) MaterialTheme.colorScheme.error else Color(0xFF388E3C)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Sinopsis",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = p.descripcion,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
