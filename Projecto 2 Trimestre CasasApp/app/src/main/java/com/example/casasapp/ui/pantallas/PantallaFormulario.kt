package com.example.casasapp.ui.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.casasapp.data.Casa
import com.example.casasapp.ui.theme.AzulMedio
import com.example.casasapp.ui.theme.DoradoSuave
import com.example.casasapp.ui.viewmodel.CasasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormulario(navController: NavController, viewModel: CasasViewModel, casaId: Int? = null) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    
    // Nuevos campos
    var habitaciones by remember { mutableStateOf(0) }
    var banos by remember { mutableStateOf(0) }
    var tieneTerreno by remember { mutableStateOf(false) }
    var tienePiscina by remember { mutableStateOf(false) }
    var tieneGaraje by remember { mutableStateOf(false) }

    var nombreError by remember { mutableStateOf(false) }
    var descripcionError by remember { mutableStateOf(false) }
    var intentoGuardar by remember { mutableStateOf(false) }
    
    var casaAEditar by remember { mutableStateOf<Casa?>(null) }
    
    // Cargar datos si estamos editando
    LaunchedEffect(casaId) {
        if (casaId != null) {
            viewModel.getCasaById(casaId).collect { casa ->
                if (casa != null) {
                    casaAEditar = casa
                    nombre = casa.nombre
                    descripcion = casa.descripcion
                    if (casa.imagen.isNotEmpty()) {
                        imagenUri = Uri.parse(casa.imagen)
                    }
                    habitaciones = casa.habitaciones
                    banos = casa.banos
                    tieneTerreno = casa.tieneTerreno
                    tienePiscina = casa.tienePiscina
                    tieneGaraje = casa.tieneGaraje
                }
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) imagenUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (casaId != null) "Editar Vivienda" else "Nueva Vivienda",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulMedio,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                     value = nombre,
                     onValueChange = {
                         nombre = it
                         if (intentoGuardar) nombreError = it.isBlank()
                     },
                     label = { Text("Nombre de la vivienda") },
                     placeholder = { Text("Ej: Casa de campo en Toledo") },
                     isError = nombreError,
                     singleLine = true,
                     modifier = Modifier.fillMaxWidth(),
                     shape = RoundedCornerShape(12.dp),
                     colors = OutlinedTextFieldDefaults.colors(
                         focusedBorderColor = AzulMedio,
                         cursorColor = AzulMedio
                     )
                )
                if (nombreError) {
                    Text(
                        text = "El nombre no puede estar vacío",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            item {
                OutlinedTextField(
                     value = descripcion,
                     onValueChange = {
                         descripcion = it
                         if (intentoGuardar) descripcionError = it.length < 10
                     },
                     label = { Text("Descripción") },
                     placeholder = { Text("Describe la vivienda (mínimo 10 caracteres)") },
                     isError = descripcionError,
                     minLines = 3,
                     maxLines = 5,
                     modifier = Modifier.fillMaxWidth(),
                     shape = RoundedCornerShape(12.dp),
                     colors = OutlinedTextFieldDefaults.colors(
                         focusedBorderColor = AzulMedio,
                         cursorColor = AzulMedio
                     )
                )
                if (descripcionError) {
                    Text(
                        text = "La descripción debe tener al menos 10 caracteres (${descripcion.length}/10)",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            item {
                Text("Número de Habitaciones", style = MaterialTheme.typography.titleMedium)
                StepperControl(valor = habitaciones, onValorBaja = { if (habitaciones > 0) habitaciones-- }, onValorSube = { habitaciones++ })
            }

            item {
                Text("Número de Baños", style = MaterialTheme.typography.titleMedium)
                StepperControl(valor = banos, onValorBaja = { if (banos > 0) banos-- }, onValorSube = { banos++ })
            }

            item {
                Text("Características Adicionales", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                CheckboxRow(label = "Tiene Terreno", checked = tieneTerreno, onCheckedChange = { tieneTerreno = it })
                CheckboxRow(label = "Tiene Piscina", checked = tienePiscina, onCheckedChange = { tienePiscina = it })
                CheckboxRow(label = "Tiene Garaje", checked = tieneGaraje, onCheckedChange = { tieneGaraje = it })
            }

            item {
                OutlinedButton(
                     onClick = { launcher.launch("image/*") },
                     modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                     shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = if (imagenUri != null) "Cambiar imagen" else "Seleccionar imagen")
                }

                imagenUri?.let { uri ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Image(
                        painter = rememberAsyncImagePainter(model = uri),
                        contentDescription = "Previsualización de la imagen",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Imagen seleccionada ✓",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        intentoGuardar = true
                        nombreError = nombre.isBlank()
                        descripcionError = descripcion.length < 10

                        if (!nombreError && !descripcionError) {
                            val nuevaCasa = Casa(
                                id = casaAEditar?.id ?: 0,
                                nombre = nombre,
                                descripcion = descripcion,
                                imagen = imagenUri?.toString() ?: "",
                                habitaciones = habitaciones,
                                banos = banos,
                                tieneTerreno = tieneTerreno,
                                tienePiscina = tienePiscina,
                                tieneGaraje = tieneGaraje
                            )
                            if (casaId != null) {
                                viewModel.updateCasa(nuevaCasa)
                            } else {
                                viewModel.addCasa(nuevaCasa)
                            }
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DoradoSuave,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = if (casaId != null) "Actualizar Vivienda" else "Guardar Vivienda",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun StepperControl(valor: Int, onValorBaja: () -> Unit, onValorSube: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        IconButton(onClick = onValorBaja, modifier = Modifier.size(40.dp)) {
            Icon(Icons.Default.Remove, contentDescription = "Bajar")
        }
        Text(
            text = valor.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp).width(24.dp),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = onValorSube, modifier = Modifier.size(40.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Subir")
        }
    }
}

@Composable
fun CheckboxRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = AzulMedio, checkedTrackColor = AzulMedio.copy(alpha = 0.5f))
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
