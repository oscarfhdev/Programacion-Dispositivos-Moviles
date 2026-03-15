package com.example.cineapp.ui.pantallas

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import com.example.cineapp.data.Pelicula
import com.example.cineapp.ui.viewmodel.CineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormulario(
    viewModel: CineViewModel,
    peliculaId: Int, // 0 si es nueva
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf("") }
    var duracionMinutos by remember { mutableIntStateOf(120) }
    var añoLanzamiento by remember { mutableIntStateOf(2025) }
    var estaAlquilada by remember { mutableStateOf(false) }
    var formatoFisico by remember { mutableStateOf(true) }

    // Validacion visual
    val isTituloError = titulo.isBlank()
    val isDescripcionError = descripcion.length < 10

    // Para evitar mostrar el error sin que el usuario haya tocado, podemos saber si intentó guardar
    var intentoGuardar by remember { mutableStateOf(false) }

    val contentResolver = context.contentResolver
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imagenUri = it.toString()
        }
    }

    LaunchedEffect(peliculaId) {
        if (peliculaId != 0) {
            viewModel.obtenerPelicula(peliculaId).collect { pelicula ->
                pelicula?.let {
                    titulo = it.titulo
                    descripcion = it.descripcion
                    imagenUri = it.imagen
                    duracionMinutos = it.duracionMinutos
                    añoLanzamiento = it.añoLanzamiento
                    estaAlquilada = it.estaAlquilada
                    formatoFisico = it.formatoFisico
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (peliculaId == 0) "Nueva Película" else "Editar Película", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen Picker
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { imagePickerLauncher.launch(arrayOf("image/*")) },
                contentAlignment = Alignment.Center
            ) {
                if (imagenUri.isNotEmpty()) {
                    AsyncImage(
                        model = Uri.parse(imagenUri),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = "Icono galería",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text("Tocar para añadir portada", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Título
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                isError = intentoGuardar && isTituloError,
                supportingText = { if (intentoGuardar && isTituloError) Text("El título no puede estar vacío") }
            )

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                isError = intentoGuardar && isDescripcionError,
                supportingText = { if (intentoGuardar && isDescripcionError) Text("La descripción debe tener al menos 10 caracteres") }
            )

            // Stepper Duración
            StepperInt(
                label = "Duración (minutos)",
                value = duracionMinutos,
                onValueChange = { duracionMinutos = it },
                range = 1..500
            )

            // Stepper Año Lanzamiento
            StepperInt(
                label = "Año de Lanzamiento",
                value = añoLanzamiento,
                onValueChange = { añoLanzamiento = it },
                range = 1900..2100
            )

            // Toggle para Esta Alquilada
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "¿Está Alquilada?", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = estaAlquilada,
                    onCheckedChange = { estaAlquilada = it }
                )
            }

            // Checkbox para Formato Físico / Digital
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = formatoFisico,
                    onCheckedChange = { formatoFisico = it }
                )
                Text(
                    text = "Formato Físico (Blu-ray / DVD)",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Guardar
            Button(
                onClick = {
                    intentoGuardar = true
                    if (!isTituloError && !isDescripcionError) {
                        val peliculaGuardada = Pelicula(
                            id = peliculaId,
                            titulo = titulo,
                            descripcion = descripcion,
                            imagen = imagenUri,
                            duracionMinutos = duracionMinutos,
                            añoLanzamiento = añoLanzamiento,
                            estaAlquilada = estaAlquilada,
                            formatoFisico = formatoFisico
                        )
                        if (peliculaId == 0) {
                            viewModel.guardarPelicula(peliculaGuardada)
                        } else {
                            viewModel.actualizarPelicula(peliculaGuardada)
                        }
                        onVolver()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Guardar Película")
            }
        }
    }
}

@Composable
fun StepperInt(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {

            // Botón de Restar
            RepeatingIconButton(
                onClick = { if (value > range.first) onValueChange(value - 1) },
                icon = Icons.Filled.Remove,
                contentDescription = "Restar"
            )

            Text(
                text = value.toString(),
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )

            // Botón de Sumar
            RepeatingIconButton(
                onClick = { if (value < range.last) onValueChange(value + 1) },
                icon = Icons.Filled.Add,
                contentDescription = "Sumar"
            )
        }
    }
}

@Composable
fun RepeatingIconButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(50))
            .pointerInput(Unit) {
                androidx.compose.foundation.gestures.awaitEachGesture {
                    awaitPointerEvent(androidx.compose.ui.input.pointer.PointerEventPass.Main)
                    // Primera pulsación ("clic" normal)
                    onClick()

                    val job = coroutineScope.launch {
                        delay(400) // tiempo inicial para empezar a repetir
                        while (true) {
                            onClick()
                            delay(50) // velocidad de incremento (cada 50ms)
                        }
                    }

                    // Esperar a que suelte el botón
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes.any { !it.pressed }) {
                            job.cancel()
                            break
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = contentDescription, tint = MaterialTheme.colorScheme.primary)
    }
}
