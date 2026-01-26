package com.example.casasapp.ui.pantallas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.casasapp.ui.viewmodel.CasasViewModel

@Composable
fun PantallaGaleria(navController: NavController, viewModel: CasasViewModel) {
    val casas by viewModel.casas.collectAsState()
    Column {
        Button(onClick = { navController.navigate("formulario") }) {
            Text("Añadir casa")
        }
        LazyColumn {
            items(casas) {
                casa -> Text(text = casa.nombre)
            }
        }
    }
}
