package com.example.casasapp.ui.pantallas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.casasapp.data.Casa
import com.example.casasapp.ui.viewmodel.CasasViewModel

@Composable
fun PantallaFormulario(navController: NavController, viewModel: CasasViewModel) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var nombreError by remember { mutableStateOf(false) }
    var descripcionError by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imagenUri = uri
    }

    Column {
        OutlinedTextField(
            value = nombre,
            onValueChange = { 
                nombre = it
                nombreError = it.isBlank()
            },
            label = { Text("Nombre") },
            isError = nombreError
        )
        OutlinedTextField(
            value = descripcion,
            onValueChange = { 
                descripcion = it
                descripcionError = it.length < 10
             },
            label = { Text("Descripción") },
            isError = descripcionError
        )
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        imagenUri?.let {
            Text("Imagen seleccionada:")
            //Image(painter = rememberAsyncImagePainter(model = it), contentDescription = null)
        }

        Button(onClick = {
            if (nombre.isNotBlank() && descripcion.length >= 10) {
                val nuevaCasa = Casa(
                    nombre = nombre, 
                    descripcion = descripcion, 
                    imagen = imagenUri?.toString() ?: ""
                )
                viewModel.addCasa(nuevaCasa)
                navController.popBackStack()
            }
        }) {
            Text("Guardar")
        }
    }
}
