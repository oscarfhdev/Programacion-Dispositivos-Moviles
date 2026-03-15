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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    var duracionText by remember { mutableStateOf("120") }
    var fechaLanzamiento by remember { mutableStateOf("") }
    var estaAlquilada by remember { mutableStateOf(false) }
    var formatoFisico by remember { mutableStateOf(true) }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

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
                    duracionText = it.duracionMinutos.toString()
                    fechaLanzamiento = it.fechaLanzamiento
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

            // Input Duración
            OutlinedTextField(
                value = duracionText,
                onValueChange = { duracionText = it.filter { char -> char.isDigit() } },
                label = { Text("Duración (minutos)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = intentoGuardar && duracionText.isBlank(),
                supportingText = { if (intentoGuardar && duracionText.isBlank()) Text("Introduce la duración") }
            )

            // Input Fecha Lanzamiento
            Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                OutlinedTextField(
                    value = fechaLanzamiento,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha de Lanzamiento") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false, // Previene enfoque de teclado
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = if (intentoGuardar && fechaLanzamiento.isBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                        disabledLabelColor = if (intentoGuardar && fechaLanzamiento.isBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            if (intentoGuardar && fechaLanzamiento.isBlank()) {
                Text("Selecciona una fecha de lanzamiento", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val date = Date(millis)
                                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                fechaLanzamiento = format.format(date)
                            }
                            showDatePicker = false
                        }) { Text("Aceptar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

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
                    if (!isTituloError && !isDescripcionError && duracionText.isNotBlank() && fechaLanzamiento.isNotBlank()) {
                        val peliculaGuardada = Pelicula(
                            id = peliculaId,
                            titulo = titulo,
                            descripcion = descripcion,
                            imagen = imagenUri,
                            duracionMinutos = duracionText.toIntOrNull() ?: 0,
                            fechaLanzamiento = fechaLanzamiento,
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


